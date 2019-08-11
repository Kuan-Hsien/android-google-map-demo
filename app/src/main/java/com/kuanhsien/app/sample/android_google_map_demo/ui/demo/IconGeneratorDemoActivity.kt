/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kuanhsien.app.sample.android_google_map_demo.ui.demo

import android.graphics.Color
import android.graphics.Typeface.BOLD
import android.graphics.Typeface.ITALIC
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.StyleSpan
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.ui.IconGenerator
import com.kuanhsien.app.sample.android_google_map_demo.ui.base.BaseDemoActivity

class IconGeneratorDemoActivity : BaseDemoActivity() {

    override fun startDemo() {
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-33.8696, 151.2094), 10f))

        // 1. Default
        val iconFactory = IconGenerator(this)
        addIcon(iconFactory, "Default", LatLng(-33.8696, 151.2094))

        // 2. Custom color
        iconFactory.setColor(Color.CYAN)
        addIcon(iconFactory, "Custom color", LatLng(-33.9360, 151.2070))

        // 3. Rotate the window with its arrow 90 degrees
        iconFactory.setRotation(90)
        iconFactory.setStyle(IconGenerator.STYLE_RED)
        addIcon(iconFactory, "Rotated 90 degrees", LatLng(-33.8858, 151.096))

        // 4. Rotate the content 90 degrees, but keep the window and arrow
        iconFactory.setRotation(0)
        iconFactory.setContentRotation(90)
        iconFactory.setStyle(IconGenerator.STYLE_GREEN)
        addIcon(iconFactory, "ContentRotate=90", LatLng(-33.7677, 151.244))

        // 5. Simultaneously rotated 90 info window and contents
        iconFactory.setContentRotation(-90)
        iconFactory.setStyle(IconGenerator.STYLE_PURPLE)
        addIcon(iconFactory, "Rotate=90, ContentRotate=-90", LatLng(-33.9992, 151.098))

        // 6. Mixing different fonts
        iconFactory.setRotation(0)
        iconFactory.setContentRotation(0)
        iconFactory.setStyle(IconGenerator.STYLE_ORANGE)
        addIcon(iconFactory, makeCharSequence(), LatLng(-33.77720, 151.12412))
    }

    private fun addIcon(iconFactory: IconGenerator, text: CharSequence, position: LatLng) {
        val markerOptions =
            MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text)))
                .position(position)
                .anchor(iconFactory.anchorU, iconFactory.anchorV)

        googleMap?.addMarker(markerOptions)
    }

    // Mixing different fonts
    private fun makeCharSequence(): CharSequence {
        val prefix = "Mixing "
        val suffix = "different fonts"
        val sequence = prefix + suffix
        val ssb = SpannableStringBuilder(sequence)
        ssb.setSpan(StyleSpan(ITALIC), 0, prefix.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        ssb.setSpan(StyleSpan(BOLD), prefix.length, sequence.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ssb
    }
}