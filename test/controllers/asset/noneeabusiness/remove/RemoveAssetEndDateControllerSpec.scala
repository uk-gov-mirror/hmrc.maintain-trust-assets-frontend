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

package controllers.asset.noneeabusiness.remove

import java.time.LocalDate

import base.SpecBase
import connectors.TrustsConnector
import controllers.Assets.OK
import forms.EndDateFormProvider
import models.NonUkAddress
import models.assets._
import org.mockito.Matchers.any
import org.mockito.Mockito.when
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.inject.bind
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.http.HttpResponse
import views.html.asset.noneeabusiness.remove.RemoveAssetEndDateView

import scala.concurrent.Future

class RemoveAssetEndDateControllerSpec extends SpecBase with ScalaCheckPropertyChecks with ScalaFutures {

  private val formProvider = new EndDateFormProvider(frontendAppConfig)
  private val prefix: String = "nonEeaBusiness.endDate"
  private val form = formProvider.withPrefix(prefix)

  lazy val formRoute = routes.RemoveAssetEndDateController.onSubmit(0)

  private val validAnswer: LocalDate = LocalDate.parse("1996-02-03")

  val mockConnector: TrustsConnector = mock[TrustsConnector]

  def createAsset(id: Int, provisional : Boolean) =
    NonEeaBusinessType(None, s"OrgName $id", NonUkAddress("", "", None, ""), "", LocalDate.now, None, provisional)

  val nonEeaAssets = List(
    createAsset(0, provisional = false),
    createAsset(1, provisional = true),
    createAsset(2, provisional = true)
  )

  "RemoveAssetEndDateController" when {

    "return OK and the correct view for a GET" in {

      val index = 0

      when(mockConnector.getAssets(any())(any(), any()))
        .thenReturn(Future.successful(Assets(Nil, Nil, Nil, Nil, Nil, Nil, nonEeaAssets)))

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers))
        .overrides(bind[TrustsConnector].toInstance(mockConnector))
        .build()

      val request = FakeRequest(GET, routes.RemoveAssetEndDateController.onPageLoad(index).url)

      val result = route(application, request).value

      val view = application.injector.instanceOf[RemoveAssetEndDateView]

      status(result) mustEqual OK

      contentAsString(result) mustEqual view(form, index, s"OrgName $index")(request, messages).toString

      application.stop()
    }

    "removing a new asset" must {

      "redirect to the add to page, removing the asset" in {

        val index = 2

        val application = applicationBuilder(userAnswers = Some(emptyUserAnswers))
          .overrides(bind[TrustsConnector].toInstance(mockConnector))
          .build()

        when(mockConnector.getAssets(any())(any(), any()))
          .thenReturn(Future.successful(Assets(Nil, Nil, Nil, Nil, Nil, Nil, nonEeaAssets)))

        when(mockConnector.removeAsset(any(), any())(any(), any()))
          .thenReturn(Future.successful(HttpResponse(200, "")))

        val request =
          FakeRequest(POST, routes.RemoveAssetEndDateController.onSubmit(index).url)
            .withFormUrlEncodedBody(
              "value.day" -> validAnswer.getDayOfMonth.toString,
              "value.month" -> validAnswer.getMonthValue.toString,
              "value.year" -> validAnswer.getYear.toString
            )

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER

        redirectLocation(result).value mustEqual controllers.asset.routes.AddAssetsController.onPageLoad().url

        application.stop()
      }
    }

    "removing an old asset" must {

      "redirect to the add to page, removing the beneficiary" in {

        val index = 0

        val application = applicationBuilder(userAnswers = Some(emptyUserAnswers))
          .overrides(bind[TrustsConnector].toInstance(mockConnector))
          .build()

        when(mockConnector.getAssets(any())(any(), any()))
          .thenReturn(Future.successful(Assets(Nil, Nil, Nil, Nil, Nil, Nil, nonEeaAssets)))

        when(mockConnector.removeAsset(any(), any())(any(), any()))
          .thenReturn(Future.successful(HttpResponse(200, "")))

        val request =
          FakeRequest(POST, routes.RemoveAssetEndDateController.onSubmit(index).url)
            .withFormUrlEncodedBody(
              "value.day" -> validAnswer.getDayOfMonth.toString,
              "value.month" -> validAnswer.getMonthValue.toString,
              "value.year" -> validAnswer.getYear.toString
            )

        val result = route(application, request).value

        status(result) mustEqual SEE_OTHER

        redirectLocation(result).value mustEqual controllers.asset.routes.AddAssetsController.onPageLoad().url

        application.stop()
      }
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val index = 0

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).overrides(bind[TrustsConnector].toInstance(mockConnector)).build()

      val request =
        FakeRequest(POST, routes.RemoveAssetEndDateController.onSubmit(index).url)
          .withFormUrlEncodedBody(("value", "invalid value"))

      val boundForm = form.bind(Map("value" -> "invalid value"))

      val view = application.injector.instanceOf[RemoveAssetEndDateView]

      val result = route(application, request).value

      status(result) mustEqual BAD_REQUEST

      contentAsString(result) mustEqual
        view(boundForm, index, s"OrgName $index")(request, messages).toString

      application.stop()
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      val index = 0

      val application = applicationBuilder(userAnswers = None).build()

      val request = FakeRequest(GET, routes.RemoveAssetEndDateController.onPageLoad(index).url)

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual controllers.routes.SessionExpiredController.onPageLoad().url

      application.stop()
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val index = 0

      val application = applicationBuilder(userAnswers = None).build()

      val request =
        FakeRequest(POST, routes.RemoveAssetEndDateController.onSubmit(index).url)
          .withFormUrlEncodedBody(
            "value.day" -> validAnswer.getDayOfMonth.toString,
            "value.month" -> validAnswer.getMonthValue.toString,
            "value.year" -> validAnswer.getYear.toString
          )

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual controllers.routes.SessionExpiredController.onPageLoad().url

      application.stop()
    }
  }
}