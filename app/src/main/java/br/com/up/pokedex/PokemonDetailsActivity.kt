package br.com.up.pokedex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.up.pokedex.model.PokeResponse
import br.com.up.pokedex.model.Pokemon
import br.com.up.pokedex.network.PokeApi
import br.com.up.pokedex.network.PokeApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PokemonDetailsActivity : AppCompatActivity() {

    private var retrofit: Retrofit? = null
    private var pokeService: PokeApiService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_details)

        val pokemon = intent.getStringExtra("pokemon")

        PokeApi().getPokemonByName(pokemon!!){
                pokemon ->

            if(pokemon != null){


                    retrofit = Retrofit.Builder()
                        .baseUrl("https://pokeapi.co/api/v2/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    pokeService = retrofit?.create(PokeApiService::class.java)

                }

                fun getPokemons(listener:(List<Pokemon>?) -> Unit){
                    val call = pokeService?.getPokemons()
                    call?.enqueue(object : Callback<PokeResponse> {
                        override fun onResponse(
                            call: Call<PokeResponse>,
                            response: Response<PokeResponse>
                        ) {
                            listener(response.body()?.pokemons)
                        }
                        override fun onFailure(call: Call<PokeResponse>,
                                               t: Throwable) {
                            listener(null)
                        }
                    })
                }

                fun getPokemonByName(name:String,
                                     listener: (Pokemon?) -> Unit){

                    val call = pokeService?.getPokemonByName(name)

                    call?.enqueue(object : Callback<Pokemon> {
                        override fun onResponse(call: Call<Pokemon>,
                                                response: Response<Pokemon>
                        ) {
                            listener(response.body())
                        }

                        override fun onFailure(call: Call<Pokemon>,
                                               t: Throwable) {
                            listener(null)
                        }
                    })

                }
            }

            }
        }


