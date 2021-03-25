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

package views.asset.shares

import forms.QuantityFormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.LongViewBehaviours
import views.html.asset.shares.SharePortfolioQuantityInTrustView

class SharePortfolioQuantityInTrustViewSpec extends LongViewBehaviours {

  private val messageKeyPrefix: String = "shares.portfolioQuantityInTrust"

  override val form: Form[Long] = new QuantityFormProvider(frontendAppConfig).withPrefix(messageKeyPrefix)

  "SharePortfolioQuantityInTrust view" must {

    val view = viewFor[SharePortfolioQuantityInTrustView](Some(emptyUserAnswers))

    def applyView(form: Form[_]): HtmlFormat.Appendable =
      view.apply(form, NormalMode)(fakeRequest, messages)

    behave like normalPage(applyView(form), messageKeyPrefix)

    behave like pageWithBackLink(applyView(form))

    behave like longPage(form, applyView, messageKeyPrefix, Some(s"$messageKeyPrefix.hint"))

    behave like pageWithASubmitButton(applyView(form))

  }
}
