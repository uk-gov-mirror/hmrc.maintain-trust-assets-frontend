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

package controllers.asset

import base.SpecBase
import config.annotations.Assets
import controllers.IndexValidation
import controllers.routes._
import forms.WhatKindOfAssetFormProvider
import models.WhatKindOfAsset
import models.WhatKindOfAsset._
import navigation.Navigator
import pages.asset.WhatKindOfAssetPage
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers.{route, _}
import views.html.asset.WhatKindOfAssetView

class WhatKindOfAssetControllerSpec extends SpecBase with IndexValidation {

  private val index = 0

  private def whatKindOfAssetRoute(index: Int = index): String = routes.WhatKindOfAssetController.onPageLoad(index).url

  private val formProvider = new WhatKindOfAssetFormProvider()
  private val form = formProvider()

  private val optionsFor5mld = WhatKindOfAsset.options()
  private val optionsFor4mld = optionsFor5mld.filterNot(_.value == NonEeaBusiness.toString)

  "WhatKindOfAsset Controller" when {

    "4mld" must {

      val options = optionsFor4mld
      val baseAnswers = emptyUserAnswers.copy(is5mldEnabled = false)

      "return OK and the correct view for a GET" in {

        val application = applicationBuilder(userAnswers = Some(baseAnswers)).build()

        val request = FakeRequest(GET, whatKindOfAssetRoute())

        val result = route(application, request).value

        val view = application.injector.instanceOf[WhatKindOfAssetView]

        status(result) mustEqual OK

        contentAsString(result) mustEqual
          view(form, index, options)(request, messages).toString

        application.stop()
      }

      "populate the view correctly on a GET when the question has previously been answered" in {

        val userAnswers = baseAnswers
          .set(WhatKindOfAssetPage, Shares).success.value

        val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

        val request = FakeRequest(GET, whatKindOfAssetRoute())

        val view = application.injector.instanceOf[WhatKindOfAssetView]

        val result = route(application, request).value

        status(result) mustEqual OK

        contentAsString(result) mustEqual
          view(form.fill(Shares), index, options)(request, messages).toString

        application.stop()
      }

      "display Money if the same index is an in progress Money asset" in {

        val userAnswers = baseAnswers
          .set(WhatKindOfAssetPage, Money).success.value

        val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

        val request = FakeRequest(GET, whatKindOfAssetRoute())

        val view = application.injector.instanceOf[WhatKindOfAssetView]

        val result = route(application, request).value

        status(result) mustEqual OK

        contentAsString(result) mustEqual
          view(form.fill(Money), index, options)(request, messages).toString

        application.stop()
      }

      "return a Bad Request and errors when invalid data is submitted" in {

        val application = applicationBuilder(userAnswers = Some(baseAnswers)).build()

        val request =
          FakeRequest(POST, whatKindOfAssetRoute())
            .withFormUrlEncodedBody(("value", "invalid value"))

        val boundForm = form.bind(Map("value" -> "invalid value"))

        val view = application.injector.instanceOf[WhatKindOfAssetView]

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST

        contentAsString(result) mustEqual
          view(boundForm, index, options)(request, messages).toString

        application.stop()
      }
    }

    "5mld" must {

      val options = optionsFor5mld
      val baseAnswers = emptyUserAnswers.copy(is5mldEnabled = true)

      "return OK and the correct view for a GET" in {

        val application = applicationBuilder(userAnswers = Some(baseAnswers)).build()

        val request = FakeRequest(GET, whatKindOfAssetRoute())

        val result = route(application, request).value

        val view = application.injector.instanceOf[WhatKindOfAssetView]

        status(result) mustEqual OK

        contentAsString(result) mustEqual
          view(form, index, options)(request, messages).toString

        application.stop()
      }

      "populate the view correctly on a GET when the question has previously been answered" in {

        val userAnswers = baseAnswers
          .set(WhatKindOfAssetPage, Shares).success.value

        val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

        val request = FakeRequest(GET, whatKindOfAssetRoute())

        val view = application.injector.instanceOf[WhatKindOfAssetView]

        val result = route(application, request).value

        status(result) mustEqual OK

        contentAsString(result) mustEqual
          view(form.fill(Shares), index, options)(request, messages).toString

        application.stop()
      }

      "display Money if the same index is an in progress Money asset" in {

        val userAnswers = baseAnswers
          .set(WhatKindOfAssetPage, Money).success.value

        val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

        val request = FakeRequest(GET, whatKindOfAssetRoute())

        val view = application.injector.instanceOf[WhatKindOfAssetView]

        val result = route(application, request).value

        status(result) mustEqual OK

        contentAsString(result) mustEqual
          view(form.fill(Money), index, options)(request, messages).toString

        application.stop()
      }

      "return a Bad Request and errors when invalid data is submitted" in {

        val application = applicationBuilder(userAnswers = Some(baseAnswers)).build()

        val request =
          FakeRequest(POST, whatKindOfAssetRoute())
            .withFormUrlEncodedBody(("value", "invalid value"))

        val boundForm = form.bind(Map("value" -> "invalid value"))

        val view = application.injector.instanceOf[WhatKindOfAssetView]

        val result = route(application, request).value

        status(result) mustEqual BAD_REQUEST

        contentAsString(result) mustEqual
          view(boundForm, index, options)(request, messages).toString

        application.stop()
      }
    }

    "redirect to the next page when valid data is submitted" in {

      val application =
        applicationBuilder(userAnswers = Some(emptyUserAnswers))
          .overrides(bind[Navigator].qualifiedWith(classOf[Assets]).toInstance(fakeNavigator))
          .build()

      val request =
        FakeRequest(POST, whatKindOfAssetRoute())
          .withFormUrlEncodedBody(("value", WhatKindOfAsset.options().head.value))

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual fakeNavigator.desiredRoute.url

      application.stop()
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      val request = FakeRequest(GET, whatKindOfAssetRoute())

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual SessionExpiredController.onPageLoad().url

      application.stop()
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      val request =
        FakeRequest(POST, whatKindOfAssetRoute())
          .withFormUrlEncodedBody(("value", WhatKindOfAsset.values.head.toString))

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual SessionExpiredController.onPageLoad().url

      application.stop()
    }

  }
}
