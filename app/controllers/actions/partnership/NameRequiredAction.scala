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

package controllers.actions.partnership

import controllers.actions.NameRequest
import javax.inject.Inject
import models.requests.DataRequest
import pages.asset.partnership.PartnershipDescriptionPage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.ActionTransformer

import scala.concurrent.{ExecutionContext, Future}

class NameRequiredAction @Inject()(val executionContext: ExecutionContext, val messagesApi: MessagesApi)
  extends ActionTransformer[DataRequest, NameRequest] with I18nSupport {

  override protected def transform[A](request: DataRequest[A]): Future[NameRequest[A]] = {
    Future.successful(NameRequest[A](request,
      getName(request)
    ))
  }

  private def getName[A](request: DataRequest[A]): String = {
    request.userAnswers.get(PartnershipDescriptionPage) match {
      case Some(name) => name
      case _ => request.messages(messagesApi)("partnership.name.default")
    }
  }
}
