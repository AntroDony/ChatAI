package com.ancraz.chatai.data.backend.superbase

import com.ancraz.chatai.data.Constants
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.realtime.Realtime

class SupabaseClient {

    val supabase = createSupabaseClient(
        Constants.SUPABASE_URL,
        Constants.SUPABASE_KEY
    ){
        //install(Auth)
        install(Postgrest)
        install(Realtime)
    }
}