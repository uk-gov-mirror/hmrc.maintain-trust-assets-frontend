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

package navigation

import base.SpecBase
import controllers.asset.other.routes._
import generators.Generators
import models.{NormalMode, UserAnswers}
import org.scalacheck.Arbitrary.arbitrary
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import pages.asset.other._

class OtherNavigatorSpec extends SpecBase with ScalaCheckPropertyChecks with Generators {

  private val navigator: Navigator = injector.instanceOf[OtherNavigator]

  "Other Navigator" must {

    "go to other asset value from other asset description" in {

      forAll(arbitrary[UserAnswers]) {
        userAnswers =>

          val answers = userAnswers.set(OtherAssetDescriptionPage, "Description").success.value

          navigator.nextPage(OtherAssetDescriptionPage, NormalMode, answers)
            .mustBe(OtherAssetValueController.onPageLoad(NormalMode))
      }
    }

    "go to check answers from other asset value" in {

      forAll(arbitrary[UserAnswers]) {
        userAnswers =>

          val answers = userAnswers.set(OtherAssetValuePage, 4000L).success.value

          navigator.nextPage(OtherAssetValuePage, NormalMode, answers)
            .mustBe(OtherAssetAnswersController.onPageLoad())
      }
    }
  }

}
