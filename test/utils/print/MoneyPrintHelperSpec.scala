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

import base.SpecBase
import controllers.asset.money.routes._
import controllers.asset.routes.WhatKindOfAssetController
import models.{NormalMode, UserAnswers}
import models.WhatKindOfAsset.Money
import pages.asset.WhatKindOfAssetPage
import pages.asset.money._
import play.twirl.api.Html
import viewmodels.{AnswerRow, AnswerSection}

class MoneyPrintHelperSpec extends SpecBase {

  private val helper: MoneyPrintHelper = injector.instanceOf[MoneyPrintHelper]
  private val index: Int = 0
  private val name: String = "Name"
  private val amount: Long = 100L

  private val answers: UserAnswers = emptyUserAnswers
    .set(WhatKindOfAssetPage, Money).success.value
    .set(AssetMoneyValuePage, amount).success.value

  private val rows: Seq[AnswerRow] = Seq(
    AnswerRow(label = Html(messages("whatKindOfAsset.first.checkYourAnswersLabel")), Html("Money"), WhatKindOfAssetController.onPageLoad(index).url),
    AnswerRow(label = Html(messages("money.value.checkYourAnswersLabel")), Html(s"£100"), AssetMoneyValueController.onPageLoad(NormalMode).url)
  )

  "MoneyPrintHelper" when {

    "generate Money Asset section" when {

      "added" in {

        val result = helper(answers, provisional = true, name)

        result mustBe AnswerSection(
          headingKey = None,
          rows = rows
        )
      }

    }

  }

}
