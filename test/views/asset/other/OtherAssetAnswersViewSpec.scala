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

package views.asset.other

import play.twirl.api.HtmlFormat
import viewmodels.AnswerSection
import views.behaviours.ViewBehaviours
import views.html.asset.other.OtherAssetAnswersView

class OtherAssetAnswersViewSpec extends ViewBehaviours {

  private val messageKeyPrefix: String = "other.answers"

  "OtherAssetAnswers view" must {

    val view = viewFor[OtherAssetAnswersView](Some(emptyUserAnswers))

    def applyView(): HtmlFormat.Appendable =
      view.apply(AnswerSection(None, Seq()))(fakeRequest, messages)

    behave like normalPage(applyView, messageKeyPrefix)

    behave like pageWithBackLink(applyView)
  }
}
