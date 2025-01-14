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

package mapping

import mapping.reads.{ShareAsset, ShareNonPortfolioAsset, SharePortfolioAsset}
import models.ShareClass
import models.assets.SharesType

class ShareAssetMapper extends Mapping[SharesType, ShareAsset] {

  override def mapAssets(assets: List[ShareAsset]): List[SharesType] = {
    assets.flatMap {
      case x: ShareNonPortfolioAsset =>
        Some(SharesType(x.quantity, x.name, ShareClass.toDES(x.`class`), x.quoted, x.value))
      case x: SharePortfolioAsset =>
        Some(SharesType(x.quantity, x.name, ShareClass.toDES(ShareClass.Other), x.quoted, x.value))
      case _ =>
        None
    }
  }
}
