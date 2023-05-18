package com.example.jetpackadmobdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.rememberAsyncImagePainter
import com.example.jetpackadmobdemo.data.AdDataItem
import com.example.jetpackadmobdemo.data.DataItem
import com.example.jetpackadmobdemo.ui.theme.JetpackAdmobDemoTheme
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this) {}
        setContent {
            JetpackAdmobDemoTheme {
                // A surface container using the 'background' color from the theme
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(color = Color.LightGray),
                ) {
                    TopAppBar(
                        title = {
                            Text(
                                getString(R.string.admob_demo),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                color = Color.White
                            )
                        },
                        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary)
                    )
                    val data = generateData()
                    AdmobBanner(modifier = Modifier.fillMaxWidth())
                    MyComposeList(
                        data = data, modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )

                }

            }
        }
    }

    fun generateData(): MutableList<Any> {
        val data = mutableListOf<Any>()
        data.add(
            DataItem(
                "Tesla Model S",
                "https://stimg.cardekho.com/images/carexteriorimages/930x620/Tesla/Model-S/5252/1611840999494/front-left-side-47.jpg"
            )
        )
        data.add(AdDataItem("Test 1"))
        data.add(
            DataItem(
                "Testla Model 3",
                "https://images.91wheels.com//assets/c_images/gallery/tesla/model-3/tesla-model-3-0-1626249225.jpg"
            )
        )
        data.add(AdDataItem("Test 2"))
        data.add(
            DataItem(
                "Tesla CyberTruck",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/0/0c/Tesla_Cybertruck_outside_unveil_modified_by_Smnt.jpg/640px-Tesla_Cybertruck_outside_unveil_modified_by_Smnt.jpg"
            )
        )
        return data;
    }

    private fun showInterstialAd() {
        InterstitialAd.load(
            this,
            "ca-app-pub-3940256099942544/1033173712", //Change this with your own AdUnitID!
            AdRequest.Builder().build(),
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    interstitialAd.show(this@MainActivity)
                }
            }
        )
    }


    @Composable
    fun MyComposeList(
        modifier: Modifier = Modifier,
        data: List<Any>,
    ) {

        LazyColumn(state = rememberLazyListState()) {
            items(data.size) {
                val item = data[it]
                if (item is DataItem) {
                    MySimpleListItem(item = item)
                } else {
                    AdmobBanner(modifier)
                }
            }
        }


    }


    @Composable
    fun MySimpleListItem(item: DataItem) {
        Box(
            Modifier
                .padding(16.dp)
                .clickable {
                    showInterstialAd()
                }) {
            Card {
                Image(
                    painter = rememberAsyncImagePainter(item.image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.8f)
                )
                item.text?.let {
                    Text(
                        text = it,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black.copy(0.4f))
                            .padding(8.dp, 4.dp, 8.dp, 4.dp),
                        fontSize = 18.sp,
                        color = Color.White,
                    )
                }
            }
        }
    }

    @Composable
    fun AdmobBanner(modifier: Modifier = Modifier) {
        AndroidView(
            modifier = Modifier.fillMaxWidth(),
            factory = { context ->
                // on below line specifying ad view.
                AdView(context).apply {
                    // on below line specifying ad size
                    //adSize = AdSize.BANNER
                    // on below line specifying ad unit id
                    // currently added a test ad unit id.
                    setAdSize(AdSize.BANNER)
                    adUnitId = "ca-app-pub-3940256099942544/6300978111"
                    // calling load ad to load our ad.
                    loadAd(AdRequest.Builder().build())
                }
            }
        )
    }
}


