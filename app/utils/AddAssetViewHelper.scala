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

package utils

import controllers.asset._
import play.api.i18n.Messages
import viewmodels.{AddRow, AddToRows}
import javax.inject.Inject
import models.assets._

class AddAssetViewHelper @Inject()(assets: Assets)
                                  (implicit messages: Messages) {

  def rows: AddToRows = {

    val complete = assets.nonEEABusiness.zipWithIndex.map(x => renderNonEEABusiness(x._1, x._2))

    AddToRows(Nil, complete)
  }

  private def renderNonEEABusiness(asset: NonEeaBusinessType, index: Int): AddRow = {
    AddRow(
      name = asset.orgName,
      typeLabel = messages(s"entities.asset.nonEeaBusiness"),
      changeUrl = noneeabusiness.amend.routes.AnswersController.extractAndRender(index).url,
      removeUrl = noneeabusiness.remove.routes.RemoveAssetYesNoController.onPageLoad(index).url
    )
  }
}
