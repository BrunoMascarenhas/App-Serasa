package br.com.mercante.api.repository

import br.com.mercante.api.model.dto.VendaPorFornecodorDTO
import br.com.mercante.api.model.Fornecedor
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate

interface FornecedorRepository : JpaRepository<Fornecedor, Long> {

    @Query("""SELECT P.CODFORNEC as codFornecedor,
        F.FORNECEDOR as fornecedor,
       COUNT(DISTINCT (C.CODCLI)) as qtCliPos,
       SUM(ROUND(NVL(I.QT, 0) *
                 (NVL(I.PVENDA, 0) +
                  NVL(I.VLOUTRASDESP, 0) +
                  NVL(I.VLFRETE, 0)),
                 2))              vlTotal
FROM PCPEDI I
       inner join PCPEDC C ON C.NUMPED = I.NUMPED
       left join PCPRODUT P on P.CODPROD = I.CODPROD
       left join PCFORNEC F on F.CODFORNEC = P.CODFORNEC
       inner join PCUSUARI U on U.CODUSUR = C.CODUSUR
WHERE U.CODSUPERVISOR NOT IN ('9999')
  AND C.DATA BETWEEN :startDate AND :endDate
  AND C.CODFILIAL IN ('1', '2')
  AND C.CONDVENDA IN (1, 2, 3, 7, 9, 14, 15, 17, 18, 19, 98)
  AND NVL(I.BONIFIC, 'N') = 'N'
  AND P.CODFORNEC IN (123, 427, 375)
  AND C.DTCANCEL IS NULL
GROUP BY P.CODFORNEC, F.FORNECEDOR""", nativeQuery = true)
    fun vendasPorFornecedorAmanco(@Param("startDate") dataInicio: LocalDate,
                                  @Param("endDate") dataFim: LocalDate): List<VendaPorFornecodorDTO>
}