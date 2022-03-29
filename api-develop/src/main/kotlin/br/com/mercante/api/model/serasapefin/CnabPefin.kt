package br.com.mercante.api.model.serasapefin

class CnabPefin {
    val records: ArrayList<RecordPefin> = arrayListOf()

    fun addRecord(recordPefin: RecordPefin) {
        when (recordPefin) {
            is HeaderPefin -> {
                recordPefin.tipoRegistro = 0
                recordPefin.sequencia = 1
                this.records.add(0, recordPefin)
            }
            is DetailPefin -> {
                recordPefin.tipoRegistro = 1
                recordPefin.sequencia = getNumeroSequencia()
                this.records.add(recordPefin)
            }
            is TraillerPefin -> {
                recordPefin.tipoRegistro = 9
                recordPefin.sequencia = getNumeroSequencia()
                this.records.add(recordPefin)
            }
        }
    }

    fun getHeader(): HeaderPefin {
        val header = this.records[0]

        if (header is HeaderPefin)
            return header
        throw IllegalArgumentException("Não existe header disponível")
    }

    private fun getNumeroSequencia() = if (this.records.isEmpty()) 0 else this.records.last().sequencia + 1
}