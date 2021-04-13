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

package config

import com.google.inject.AbstractModule
import config.annotations._
import controllers.actions._
import navigation._
import repositories.{PlaybackRepository, PlaybackRepositoryImpl}
import services.{AuthenticationService, AuthenticationServiceImpl}

class Module extends AbstractModule {

  override def configure(): Unit = {

    bind(classOf[DataRetrievalAction]).to(classOf[DataRetrievalActionImpl]).asEagerSingleton()
    bind(classOf[DataRequiredAction]).to(classOf[DataRequiredActionImpl]).asEagerSingleton()

    bind(classOf[PlaybackRepository]).to(classOf[PlaybackRepositoryImpl]).asEagerSingleton()

    // For session based storage instead of cred based, change to SessionIdentifierAction
    bind(classOf[IdentifierAction]).to(classOf[AuthenticatedIdentifierAction]).asEagerSingleton()
    bind(classOf[AuthenticationService]).to(classOf[AuthenticationServiceImpl]).asEagerSingleton()

    bind(classOf[Navigator]).annotatedWith(classOf[Assets]).to(classOf[AssetsNavigator]).asEagerSingleton()
    bind(classOf[Navigator]).annotatedWith(classOf[Money]).to(classOf[MoneyNavigator]).asEagerSingleton()
    bind(classOf[Navigator]).annotatedWith(classOf[PropertyOrLand]).to(classOf[PropertyOrLandNavigator]).asEagerSingleton()
    bind(classOf[Navigator]).annotatedWith(classOf[Shares]).to(classOf[SharesNavigator]).asEagerSingleton()
    bind(classOf[Navigator]).annotatedWith(classOf[Business]).to(classOf[BusinessNavigator]).asEagerSingleton()
    bind(classOf[Navigator]).annotatedWith(classOf[Partnership]).to(classOf[PartnershipNavigator]).asEagerSingleton()
    bind(classOf[Navigator]).annotatedWith(classOf[Other]).to(classOf[OtherNavigator]).asEagerSingleton()
    bind(classOf[Navigator]).annotatedWith(classOf[NonEeaBusiness]).to(classOf[NonEeaBusinessNavigator]).asEagerSingleton()
  }
}
