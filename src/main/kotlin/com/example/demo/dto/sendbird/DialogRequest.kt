package com.example.demo.dto.sendbird

data class DialogRequest(
    val sys: List<String>, // 최근 기기의 채팅
    val usr: List<String> // 최근 유저의 채팅
)
