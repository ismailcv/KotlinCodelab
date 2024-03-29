/*
View model dosyamızı burada oluşturuyoruz.
 */

package com.example.unscramble.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.allWords
import com.example.unscramble.model.GameUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class GameViewModel : ViewModel() {
    // Game UI state
    //Değişen verileri gözlemlemek için oluşturduk
    // Bu değer sadece bu sınıf içinden değiştirilebilecek
    private val _uiState = MutableStateFlow(GameUiState())

    //Observer(gözlemciler) tarafından akışını gözlemlenebilir hale getiriyor.
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    // Kullanılmış kelimelerin listesini tutuyoruz.
    private var usedWords: MutableSet<String> = mutableSetOf()

    //mevcut kelimeyi alacak değişken oluşturduk.
    private lateinit var currentWord: String
    var userGuess by mutableStateOf("")
        private set

    //Oyunun başlangıcında oyunu sıfırlıyoruz.
    init {
        resetGame()
    }

    /*
    Kullanılmış kelimeler listesini sıfırlıyoruz.
    Ui state'in değerini currentScrambledWord = pickRandomWordAndShuffle() fonksiyonu
    ile yeniden atıyoruz.
     */
    fun resetGame() {
        usedWords.clear()
        _uiState.value = GameUiState(currentScrambledWord = pickRandomWordAndShuffle())
    }

    //Mevcut kelimeyi karıştırmak için kullanılan fonksiyon
    private fun shuffleCurrentWord(word: String): String {
        val tempWord = word.toCharArray()
        // Scramble the word
        tempWord.shuffle()
        while (String(tempWord).equals(word)) {
            tempWord.shuffle()
        }
        return String(tempWord)
    }

    //Rastgele bir kelime seçmek ve karıştırmak için kullanılan fonksiyon
    private fun pickRandomWordAndShuffle(): String {
        // Continue picking up a new random word until you get one that hasn't been used before
        currentWord = allWords.random()
        if (usedWords.contains(currentWord)) {
            return pickRandomWordAndShuffle()
        } else {
            usedWords.add(currentWord)
            return shuffleCurrentWord(currentWord)
        }
    }

    fun updateUserGuess(guessedWord: String) {
        userGuess = guessedWord
    }

    //Kullanıcının tahmini ile değerinimizi kontrol ediyor.
    fun checkUserGuess() {
        //Kullanıcı doğru tahmin ederse kor değeri artacak.
        if (userGuess.equals(currentWord, ignoreCase = true)) {
            val updatedScore = _uiState.value.score.plus(SCORE_INCREASE)
            //güncellenmiş skoru oyunun güncellenmesi için verdik.
            updateGameState(updatedScore)
        } else {
            // Kullanıcının tahimini yanlışsa hata döndürüyor
            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong = true)
            }
        }
        // Reset user guess
        updateUserGuess("")
    }

    /*
    Oyuncunun skorunu güncellemek yeni kelime seçmek için kullanacağız
     */
    private fun updateGameState(updatedScore: Int) {
        //Kullanıcı maksimum değerde kelime tahmin ettiğinde
        if (usedWords.size == MAX_NO_OF_WORDS){
            //Son rounda'da delindiğinde gameover değeri true olur ve yeni kelime gelmez
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    score = updatedScore,
                    isGameOver = true
                )
            }
        } else{
            // Normal round in the game
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    currentScrambledWord = pickRandomWordAndShuffle(),
                    currentWordCount = currentState.currentWordCount.inc(),
                    score = updatedScore
                )
            }
        }
    }

    //skip butonu için kullandık yeni cümleye geçiyoruz.
    fun skipWord() {
        updateGameState(_uiState.value.score)
        // Reset user guess
        updateUserGuess("")
    }
}