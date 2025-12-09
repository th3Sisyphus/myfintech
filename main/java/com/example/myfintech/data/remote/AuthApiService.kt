package com.example.myfintech.data.remote

// Nanti ini akan menjadi interface Retrofit (@POST, @GET, dll)
// Untuk sekarang, kita buat class dummy agar aplikasi bisa berjalan tanpa backend
class AuthApiService {

    suspend fun loginUser(email: String, password: String): Boolean {
        // SIMULASI REQUEST KE SERVER
        kotlinx.coroutines.delay(2000) // Delay 2 detik (Loading State akan muncul)

        // Logika dummy: Login sukses jika password bukan "123456"
        return password != "123456"
    }

    suspend fun registerUser(name: String, email: String): Boolean {
        kotlinx.coroutines.delay(2000)
        return true // Selalu sukses untuk demo
    }
}