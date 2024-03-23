package com.example.cupcake.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.cupcake.R

/**
 * Ekranda biçimlendirilmiş olarak gösterilecek [price]'ı görüntüleyen bir Composable.
 * Bu composable birden farklı yerde kullanılmıştır. Bizde bu yüzden bunu
 * components içine CommonUi sınıfında yazıyoruz. Başka ekranlarda
 * eğer bu composable kullanılırsa kopyala yapıştır yapmaya gerek kalmıyor.
 */
@Composable
fun FormattedPriceLabel(subtotal: String, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.subtotal_price, subtotal),
        modifier = modifier,
        style = MaterialTheme.typography.headlineSmall
    )
}
