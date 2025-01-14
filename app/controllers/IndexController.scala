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

package controllers

import connectors.TrustsConnector
import controllers.actions.StandardActionSets
import models.UserAnswers
import play.api.i18n.I18nSupport
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.PlaybackRepository
import services.FeatureFlagService
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import javax.inject.Inject
import play.api.Logging
import utils.Session

import scala.concurrent.{ExecutionContext, Future}

class IndexController @Inject()(
                                 val controllerComponents: MessagesControllerComponents,
                                 actions: StandardActionSets,
                                 cacheRepository : PlaybackRepository,
                                 connector: TrustsConnector,
                                 featureFlagService: FeatureFlagService
                               )(implicit ec: ExecutionContext) extends FrontendBaseController with I18nSupport with Logging {

  def onPageLoad(identifier: String): Action[AnyContent] = (actions.auth andThen actions.saveSession(identifier) andThen actions.getData).async {
    implicit request =>
      logger.info(s"[Session ID: ${Session.id(hc)}][UTR/URN/URN: $identifier]" +
        s" user has started to maintain assets")
      for {
        details <- connector.getTrustDetails(identifier)
        is5mldEnabled <- featureFlagService.is5mldEnabled()
        isUnderlyingData5mld <- connector.isTrust5mld(identifier)
        ua <- Future.successful(
          request.userAnswers.getOrElse {
            UserAnswers(
              internalId = request.user.internalId,
              identifier = identifier,
              whenTrustSetup = details.startDate,
              is5mldEnabled = is5mldEnabled,
              isTaxable = details.trustTaxable.getOrElse(true),
              isUnderlyingData5mld = isUnderlyingData5mld
            )
          }
        )
        _ <- cacheRepository.set(ua)
      } yield {
        Redirect(controllers.asset.routes.AddAssetsController.onPageLoad())
      }
  }

}
