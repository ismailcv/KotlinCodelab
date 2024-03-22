package com.example.dessertclicker

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.dessertclicker.data.DessertUiState
import com.example.dessertclicker.ui.DessertViewModel
import com.example.dessertclicker.ui.theme.DessertClickerTheme

//Logcat'ta yaptığımız değişiklikleri görmek için oluşturduk
//sınıfın ismini veriyoruzki neresi için kullanıldığı belli olsun
private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)/*
        Buraya geldiğinde log kaydını logcat'a yazdırıyoruz.
        Burada Log. dan sonraki harfler o logun(kaydın) cinsini belirtir.
        i (information) bilgi mesajları için,
        d (debug) ayıklama mesajları için,
        e (error) hata mesajları için
        w (warning) uyarı mesajları için
        Burada TAG değişkenine biz sınıfın adını verdik
        msg değişkeni ise asıl logcat'de yazdırılan değişkendir.
         */
        Log.d(TAG, "onCreate Called")
        //Kullanıcı arayüz düzenini belirtir.
        setContent {
            DessertClickerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .statusBarsPadding(),
                ) {
                    /*
                    veriyi önceden böyle direk uygulamanın içine veriyorduk.
                    bunu view model ile yapacağımız için böyle vermiyoruz.
                    DessertClickerApp(desserts = Datasource.dessertList)
                     */
                    DessertClickerApp()
                }
            }
        }
    }

    //Acticity lifecycle da kullandığımız metodlar bulunmakta.
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart Called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume Called")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart Called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause Called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy Called")
    }
}

//Bir viewmodel nesnesi alıyor.
@Composable
private fun DessertClickerApp(viewModel: DessertViewModel = viewModel()) {
    // ViewModel'den gelen tatlı durumu akışını toplar
    // ve bu durumu bir State olarak saklar.
    val uiState by viewModel.dessertUiState.collectAsState()

    // DessertClickerApp Composable'ini çağırır ve ViewModel'den gelen verileri aktarır.
    // Kullanıcı tatlıya tıkladığında ViewModel'deki ilgili işlevi çağırır.
    DessertClickerApp(
        uiState = uiState, onDessertClicked = viewModel::onDessertClicked
    )
}

@Composable
private fun DessertClickerApp(
    uiState: DessertUiState, onDessertClicked: () -> Unit
) {
    // AppBar için gerekli olan Context'i alır.
    val intentContext = LocalContext.current

    // Scaffold, temel yapıyı oluşturur ve içeriği düzenler.
    Scaffold(topBar = {
        //üst çubuğu oluşturan bir Composable işlevi çağırır.
        DessertClickerAppBar(onShareButtonClicked = {
            // Paylaşma düğmesine tıklandığında satılan tatlılar hakkındaki bilgileri paylaşır.
            shareSoldDessertsInformation(
                intentContext = intentContext,
                dessertsSold = uiState.dessertsSold,
                revenue = uiState.revenue
            )
        })
    }) { contentPadding ->
        // ana ekran içeriğini oluşturan bir Composable işlevi çağırır.
        DessertClickerScreen(
            revenue = uiState.revenue,
            dessertsSold = uiState.dessertsSold,
            dessertImageId = uiState.currentDessertImageId,
            onDessertClicked = onDessertClicked,
            modifier = Modifier.padding(contentPadding)
        )
    }
}

@Composable
private fun DessertClickerAppBar(
    onShareButtonClicked: () -> Unit, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.app_name),
            modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_medium)),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleLarge,
        )
        IconButton(
            onClick = onShareButtonClicked,
            modifier = Modifier.padding(end = dimensionResource(R.dimen.padding_medium)),
        ) {
            Icon(
                imageVector = Icons.Filled.Share,
                contentDescription = stringResource(R.string.share),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun DessertClickerScreen(
    revenue: Int,
    dessertsSold: Int,
    @DrawableRes dessertImageId: Int,
    onDessertClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(R.drawable.bakery_back),
            contentDescription = null,
            contentScale = ContentScale.Crop
        )
        Column {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
            ) {
                Image(
                    painter = painterResource(dessertImageId),
                    contentDescription = null,
                    modifier = Modifier
                        .width(dimensionResource(R.dimen.image_size))
                        .height(dimensionResource(R.dimen.image_size))
                        .align(Alignment.Center)
                        .clickable { onDessertClicked() },
                    contentScale = ContentScale.Crop,
                )
            }
            TransactionInfo(
                revenue = revenue, dessertsSold = dessertsSold
            )
        }
    }
}

@Composable
private fun TransactionInfo(
    revenue: Int, dessertsSold: Int, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.background(Color.White)
    ) {
        DessertsSoldInfo(
            dessertsSold = dessertsSold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium))
        )
        RevenueInfo(
            revenue = revenue,
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_medium))
        )
    }
}

@Composable
private fun RevenueInfo(revenue: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(R.string.total_revenue),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Text(
            text = "$${revenue}",
            textAlign = TextAlign.Right,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
private fun DessertsSoldInfo(dessertsSold: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(R.string.dessert_sold),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Text(
            text = dessertsSold.toString(),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

/**
 * Satılan tatlılar hakkındaki bilgileri ACTION_SEND intent'i kullanarak paylaşır.
 */
private fun shareSoldDessertsInformation(intentContext: Context, dessertsSold: Int, revenue: Int) {
    // Paylaşma amacıyla bir Intent oluşturulur.
    val sendIntent = Intent().apply {
        action = Intent.ACTION_SEND
        // Tatlıların satış bilgileri metin olarak eklenir.
        putExtra(
            Intent.EXTRA_TEXT, intentContext.getString(R.string.share_text, dessertsSold, revenue)
        )
        // Paylaş butonuna basıldığında paylaşılacak içeriğin türü belirlenir.
        type = "text/plain"
    }

    // Kullanıcıya paylaşma seçenekleri sunmak için bir Intent oluşturulur.
    val shareIntent = Intent.createChooser(sendIntent, null)

    try {
        // Paylaşma işlemi başlatılır.
        ContextCompat.startActivity(intentContext, shareIntent, null)
    } catch (e: ActivityNotFoundException) {
        // Eğer paylaşma işlemi desteklenmiyorsa bir hata mesajı gösterilir.
        Toast.makeText(
            intentContext,
            intentContext.getString(R.string.sharing_not_available),
            Toast.LENGTH_LONG
        ).show()
    }
}

@Preview
@Composable
fun MyDessertClickerAppPreview() {
    DessertClickerTheme {
        DessertClickerApp(uiState = DessertUiState(), onDessertClicked = {})
    }
}
