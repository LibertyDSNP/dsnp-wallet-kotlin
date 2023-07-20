package com.unfinished.data.util

sealed class FrequencyChain {

    object Config: FrequencyChain() {
        val ID = "496e2f8a93bf4576317f998d01480f9068014b368856ec088a63e57071cd1d49"
        var GENSIS_HASH: String = "0x5b9ae6fd47d88a084767da3d544d3334e5804c1201005c1bd6dd59f23ed57350"
    }
    
    sealed class Account {

        object Name: Account() {
            var TEST_1 = "Test1"
            var TEST_2 = "Test2"
            var TEST_3 = "Test3"
            var TEST_4 = "Test4"
        }

        object Address : Account() {
            var TEST_1 = "5CXVZbHLoDprVw2r2tWLzgNJqZZdu8rZ5dBSPiSXgSH8V3fp"
            var TEST_2 = "5CV1EtqhSyompvpNnZ2pWvcFp2rycoRVFUxAHyAuVMvb9SnG"
            var TEST_3 = "5GNQNJaWFUXQdHSYSKYQGvkzoDbt11xNgHq2xra23Noxpxyn"
            var TEST_4 = "5GNQNJaWFUXQdHSYSKYQGvkzoDbt11xNgHq2xra23Noxpxyn"
        }

        object Mnemonic: Account() {
            var TEST_1 = "nerve gesture jealous wealth rally priority apple visual mom boil evoke six"
            var TEST_2 = "forum patient museum ceiling garden raccoon license deliver spoon warrior burger comic"
            var TEST_3 = "flock whale gentle recycle gift forget welcome sadness feature mandate ice athlete"
            var TEST_4 = "clip diamond flip faith hat brave true cry can jazz dust piece"

        }
    }
    
}