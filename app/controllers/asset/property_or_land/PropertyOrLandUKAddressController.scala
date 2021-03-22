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

import config.annotations.PropertyOrLand
import controllers.actions.{DraftIdRetrievalActionProvider, RegistrationDataRequiredAction, RegistrationIdentifierAction}
import controllers.filters.IndexActionFilterProvider
import forms.UKAddressFormProvider
import navigation.Navigator
import pages.asset.property_or_land.PropertyOrLandUKAddressPage
import play.api.data.Form
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.RegistrationsRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.asset.property_or_land.PropertyOrLandUKAddressView

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}

class PropertyOrLandUKAddressController @Inject()(
                                                   override val messagesApi: MessagesApi,
                                                   repository: RegistrationsRepository,
                                                   @PropertyOrLand navigator: Navigator,
                                                   identify: RegistrationIdentifierAction,
                                                   getData: DraftIdRetrievalActionProvider,
                                                   requireData: RegistrationDataRequiredAction,
                                                   validateIndex: IndexActionFilterProvider,
                                                   formProvider: UKAddressFormProvider,
                                                   val controllerComponents: MessagesControllerComponents,
                                                   view: PropertyOrLandUKAddressView
                                                 )(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport {

  private val form = formProvider()

  private def actions(index : Int) =
    identify andThen getData() andThen
      requireData andThen
      validateIndex(index, sections.Assets)

  def onPageLoad(index : Int): Action[AnyContent] = actions(index) {
  implicit request =>

      val preparedForm = request.userAnswers.get(PropertyOrLandUKAddressPage(index)) match {
        case None => form
        case Some(value) => form.fill(value)
      }

      Ok(view(preparedForm, index))
  }

  def onSubmit(index : Int): Action[AnyContent] = actions(index).async {
    implicit request =>

      form.bindFromRequest().fold(
        (formWithErrors: Form[_]) =>
          Future.successful(BadRequest(view(formWithErrors, index))),

        value => {
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(PropertyOrLandUKAddressPage(index), value))
            _              <- repository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage(PropertyOrLandUKAddressPage(index))(updatedAnswers))
        }
      )
  }
}
