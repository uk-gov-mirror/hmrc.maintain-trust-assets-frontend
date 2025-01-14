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

package views.asset.property_or_land

import play.twirl.api.HtmlFormat
import viewmodels.AnswerSection
import views.behaviours.ViewBehaviours
import views.html.asset.property_or_land.PropertyOrLandAnswersView

class PropertyOrLandAnswerViewSpec extends ViewBehaviours {

  private val messageKeyPrefix: String = "propertyOrLand.answers"

  "PropertyOrLandAnswer view" must {

    val view = viewFor[PropertyOrLandAnswersView](Some(emptyUserAnswers))

    def applyView(): HtmlFormat.Appendable =
      view.apply(AnswerSection(None, Seq()))(fakeRequest, messages)

    behave like normalPage(applyView, messageKeyPrefix)

    behave like pageWithBackLink(applyView)
  }
}
