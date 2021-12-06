package com.zhiyong.tingxie.ui.friend

data class TingXieGroup(val name: String, val individuals: List<TingXieIndividual>) {
  constructor(name: String): this(name, arrayListOf())
}
