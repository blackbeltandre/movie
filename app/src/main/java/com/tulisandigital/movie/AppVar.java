package com.tulisandigital.movie;

public class AppVar {

    public static String BASE_URL = "https://api.themoviedb.org/3/movie/";

    public static final String BASE_IMAGE = "http://image.tmdb.org/t/p/w185";

    public static String api_key = "";
    /*masukkan keynya di String api_key c1f211c61a515335a6836a91aa0e41ee*/

    public static final String URL_MOVIE_550 = BASE_URL + "popular?api_key=" + api_key;

    public static final String URL_MOVIE_TOP_RATED = BASE_URL + "top_rated?api_key=" + api_key;

}
