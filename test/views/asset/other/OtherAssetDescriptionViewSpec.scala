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

import forms.DescriptionFormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.StringViewBehaviours
import views.html.asset.other.OtherAssetDescriptionView

class OtherAssetDescriptionViewSpec extends StringViewBehaviours {

  private val prefix: String = "other.description"
  private val hintKey: String = s"$prefix.hint"

  override val form: Form[String] = new DescriptionFormProvider().withConfig(56, prefix)

  "OtherAssetDescription view" must {

    val view = viewFor[OtherAssetDescriptionView](Some(emptyUserAnswers))

    def applyView(form: Form[_]): HtmlFormat.Appendable =
      view.apply(form, NormalMode)(fakeRequest, messages)

    behave like normalPage(applyView(form), prefix)

    behave like pageWithBackLink(applyView(form))

    behave like stringPage(form, applyView, prefix, Some(hintKey))

    behave like pageWithASubmitButton(applyView(form))

  }
}
