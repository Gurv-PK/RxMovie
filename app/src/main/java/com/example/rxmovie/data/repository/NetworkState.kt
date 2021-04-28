package com.example.rxmovie.data.repository

enum class Status{
    RUNNING,
    SUCCESS,
    FAILED,
    ENDOFLIST
}

class NetworkState(val status: Status, val msg: String) {


    companion object{


        val Loaded: NetworkState
        val Loading: NetworkState
        val Error: NetworkState
        val Endoflist: NetworkState


        init {
            Loaded = NetworkState(Status.SUCCESS,"SUCCESS")
            Loading = NetworkState(Status.RUNNING,"RUNNING")
            Error = NetworkState(Status.FAILED,"ERROR")
            Endoflist = NetworkState(Status.ENDOFLIST,"ENDOFLIST")
        }

    }




}