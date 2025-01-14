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

import config.annotations.Assets
import controllers.actions.StandardActionSets
import navigation.Navigator
import pages.asset.AssetInterruptPage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.PlaybackRepository
import views.html.asset.AssetInterruptView
import javax.inject.Inject
import models.NormalMode
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController

import scala.concurrent.{ExecutionContext, Future}

class AssetInterruptPageController @Inject()(
                                              override val messagesApi: MessagesApi,
                                              standardActionSets: StandardActionSets,
                                              repository: PlaybackRepository,
                                              @Assets navigator: Navigator,
                                              val controllerComponents: MessagesControllerComponents,
                                              assetInterruptView: AssetInterruptView
                                            )(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport {

  def onPageLoad(): Action[AnyContent] = standardActionSets.verifiedForIdentifier {
    implicit request => Ok(assetInterruptView())
  }

  def onSubmit(): Action[AnyContent] = standardActionSets.verifiedForIdentifier.async {
    implicit request =>
      for {
        updatedAnswers <- Future.fromTry(request.userAnswers.cleanup)
        _ <- repository.set(request.userAnswers)
      } yield {
        Redirect(navigator.nextPage(AssetInterruptPage, NormalMode, updatedAnswers))
      }
  }

}
