# Usage
Add a dependency to your `build.gradle`:
```groovy
dependencies {
    implementation 'ru.ralnik:myseekbarrange:1.1.3'
}
```

Default style using xml.
```groovy
<ru.ralnik.myseekbarrange.SeekbarRange
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        />
```
```groovy
<ru.ralnik.myseekbarrange.SeekbarRangeAdvance
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        />
```

Available attributes

<!--************ SeekbarRange *****************-->
- sbr_absoluteMinValue
- sbr_absoluteMaxValue
- sbr_bgSeekbarRange
- sbr_barColor
- sbr_barHighlightColor
- sbr_left_thumb_image
- sbr_right_thumb_image
- sbr_cornerRaduis

<!--************ SeekbarRangeAdvance *****************-->
- sbra_absoluteMinValue" format="float" />
- sbra_absoluteMaxValue" format="float" />
- sbra_bgSeekbarRange" format="reference"/>
- sbra_barHighlightColor" format="reference" />
- sbra_barColor" format="color" />
- sbra_left_thumb_image" format="reference" />
- sbra_right_thumb_image" format="reference" />
- sbra_show_edits" format="boolean" />
- sbra_bgEdits" format="reference" />
- sbra_heightEdits" format="integer"/>
- sbra_widthEdits" format="integer" />
- sbra_fontBoldEdits" format="boolean" />
- sbra_textSizeOfEdits" format="integer" />
- sbra_textColorOfEdits" format="color" />
- sbra_edits_ems" format="integer"></attr>
- sbra_data_type = "_integer | _float | _double"





## Changelog

##### 1.1.1 - 21.06.2019
- Add New Project.

##### 1.1.2 - 28.07.2019
- fix bugs.
- added new component "SeekbarRangeAdvance"

##### 1.1.3 - 29.07.2019
- fix bugs into SeekbarRangeAdvance class.


# LICENSE

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

##Authors
* [ralnik]
          GitHub: https://github.com/ralnik/
          e-mail: ralnik85@gmail.com

