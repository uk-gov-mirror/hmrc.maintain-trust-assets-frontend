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

package controllers.asset.property_or_land

import base.SpecBase
import controllers.routes._
import models.Status.Completed
import models.WhatKindOfAsset.PropertyOrLand
import pages.AssetStatus
import pages.asset.WhatKindOfAssetPage
import pages.asset.property_or_land._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import utils.print.PropertyOrLandPrintHelper
import views.html.asset.property_or_land.PropertyOrLandAnswersView

class PropertyOrLandAnswerControllerSpec extends SpecBase {

  private val totalValue: Long = 10000L
  val name: String = "Description"

  lazy val propertyOrLandAnswerRoute: String = routes.PropertyOrLandAnswerController.onPageLoad().url

  "PropertyOrLandAnswer Controller" must {

    "property or land does not have an address and total value is owned by the trust" must {

      "return OK and the correct view for a GET" in {

        val answers =
          emptyUserAnswers
            .set(WhatKindOfAssetPage, PropertyOrLand).success.value
            .set(PropertyOrLandAddressYesNoPage, false).success.value
            .set(PropertyOrLandDescriptionPage, "Property Land Description").success.value
            .set(PropertyOrLandTotalValuePage, totalValue).success.value
            .set(TrustOwnAllThePropertyOrLandPage, true).success.value
            .set(AssetStatus, Completed).success.value

        val application = applicationBuilder(userAnswers = Some(answers)).build()

        val request = FakeRequest(GET, propertyOrLandAnswerRoute)

        val result = route(application, request).value

        val view = application.injector.instanceOf[PropertyOrLandAnswersView]
        val printHelper = application.injector.instanceOf[PropertyOrLandPrintHelper]
        val answerSection = printHelper(answers, provisional = true, name)

        status(result) mustEqual OK

        contentAsString(result) mustEqual
          view(answerSection)(request, messages).toString

        application.stop()
      }

    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      val request = FakeRequest(GET, propertyOrLandAnswerRoute)

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual SessionExpiredController.onPageLoad().url

      application.stop()
    }

  }
}
