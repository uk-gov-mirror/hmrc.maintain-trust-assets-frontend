/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package utils.print

import controllers.asset.property_or_land.routes._
import models.UserAnswers
import pages.asset.property_or_land._
import play.api.i18n.Messages
import utils.{AnswerRowConverter, CheckAnswersFormatters}
import viewmodels.AnswerRow

import javax.inject.Inject

class PropertyOrLandPrintHelper @Inject()(checkAnswersFormatters: CheckAnswersFormatters) extends PrintHelper {

  override val assetType: String = "propertyOrLandAsset"

  override def answerRows(userAnswers: UserAnswers,
                          arg: String,
                          index: Int)
                         (implicit messages: Messages): Seq[AnswerRow] = {

    val converter: AnswerRowConverter = new AnswerRowConverter(checkAnswersFormatters)(userAnswers, arg)

    Seq(
      converter.assetTypeQuestion(index),
      converter.yesNoQuestion(PropertyOrLandAddressYesNoPage(index), "propertyOrLand.addressYesNo", PropertyOrLandAddressYesNoController.onPageLoad(index).url),
      converter.yesNoQuestion(PropertyOrLandAddressUkYesNoPage(index), "propertyOrLand.addressUkYesNo", PropertyOrLandAddressUkYesNoController.onPageLoad(index).url),
      converter.addressQuestion(PropertyOrLandUKAddressPage(index), "propertyOrLand.ukAddress", PropertyOrLandUKAddressController.onPageLoad(index).url),
      converter.addressQuestion(PropertyOrLandInternationalAddressPage(index), "propertyOrLand.internationalAddress", PropertyOrLandInternationalAddressController.onPageLoad(index).url),
      converter.stringQuestion(PropertyOrLandDescriptionPage(index), "propertyOrLand.description", PropertyOrLandDescriptionController.onPageLoad(index).url),
      converter.currencyQuestion(PropertyOrLandTotalValuePage(index), "propertyOrLand.totalValue", PropertyOrLandTotalValueController.onPageLoad(index).url),
      converter.yesNoQuestion(TrustOwnAllThePropertyOrLandPage(index), "propertyOrLand.trustOwnAllYesNo", TrustOwnAllThePropertyOrLandController.onPageLoad(index).url),
      converter.currencyQuestion(PropertyLandValueTrustPage(index), "propertyOrLand.valueInTrust", PropertyLandValueTrustController.onPageLoad(index).url)
    ).flatten
  }
}
