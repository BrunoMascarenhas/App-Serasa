package br.com.mercante.api.model

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import java.time.LocalDate
import java.time.LocalDateTime

@JacksonXmlRootElement(localName = "nfeProc")
data class NotaFiscal(
        @JacksonXmlProperty(localName = "versao")
        var versao: String,
        @JacksonXmlProperty(localName = "NFe")
        var nfe: Nfe,
        @JacksonXmlProperty(localName = "protNFe")
        var protocoloNfe: ProtocoloNfe
)

data class ProtocoloNfe(
        @JacksonXmlProperty(localName = "infProt")
        var informacoesProtocolo: InformacoesProtocolo?
)

data class InformacoesProtocolo(
        @JacksonXmlProperty(localName = "tpAmb")
        var tipoAmbient: Number,
        @JacksonXmlProperty(localName = "verAplic")
        var versaoAplicacao: String,
        @JacksonXmlProperty(localName = "chNFe")
        var chaveNfe: String,
        @JacksonXmlProperty(localName = "dhRecbto")
        var dataHoraReceimento: LocalDateTime,
        @JacksonXmlProperty(localName = "nProt")
        var numeroProtocolo: String,
        @JacksonXmlProperty(localName = "digVal")
        var digitoValidador: String,
        @JacksonXmlProperty(localName = "cStat")
        var codigoStatus: Number,
        @JacksonXmlProperty(localName = "xMotivo")
        var motivo: String
)

data class Nfe(
        @JacksonXmlProperty(localName = "infNFe")
        val informacoesNfe: InformacoesNfe
)

data class InformacoesNfe(
        @JacksonXmlProperty(localName = "ide")
        val identificacaoNfe: IdentificacaoNfe,
        @JacksonXmlProperty(localName = "emit")
        val emitente: Emitente,
        @JacksonXmlProperty(localName = "dest")
        val destinatario: Destinatario,
        @JacksonXmlProperty(localName = "det")
        @JacksonXmlElementWrapper(useWrapping = false)
        val det: List<Item>,
        @JacksonXmlProperty(localName = "total")
        val totaisIcms: TotaisIcms,
        @JacksonXmlProperty(localName = "transp")
        val transporte: Transporte,
        @JacksonXmlProperty(localName = "cobr")
        val cobranca: Cobranca,
        @JacksonXmlProperty(localName = "pag")
        val pagamento: Pagamento,
        @JacksonXmlProperty(localName = "infAdic")
        val informacoesAdicionais: InformacoesAdicionais
)

data class IdentificacaoNfe(
        @JacksonXmlProperty(localName = "cUF")
        val codigoUF: Number,
        @JacksonXmlProperty(localName = "cNF")
        val codigoNF: String,
        @JacksonXmlProperty(localName = "natOp")
        val naturezaDaOperacao: String,
        @JacksonXmlProperty(localName = "mod")
        val modelo: String,
        @JacksonXmlProperty(localName = "serie")
        val serie: String,
        @JacksonXmlProperty(localName = "nNF")
        val numeroNF: String,
        @JacksonXmlProperty(localName = "dhEmi")
        val dataHoraEmissao: LocalDateTime,
        @JacksonXmlProperty(localName = "tpNF")
        val tipoNF: String,
        @JacksonXmlProperty(localName = "idDest")
        val identificadorDestino: Number,
        @JacksonXmlProperty(localName = "cMunFG")
        val codigoMunicipioFG: Number,
        @JacksonXmlProperty(localName = "tpImp")
        val tipoDeImpressao: Number,
        @JacksonXmlProperty(localName = "tpEmis")
        val tipoEmissao: Number,
        @JacksonXmlProperty(localName = "cDV")
        val digitoVerificador: Number,
        @JacksonXmlProperty(localName = "tpAmb")
        val tipoAmbiente: Number,
        @JacksonXmlProperty(localName = "finNFe")
        val finalidade: Number,
        @JacksonXmlProperty(localName = "indFinal")
        val consumidorFinal: Number,
        @JacksonXmlProperty(localName = "indPres")
        val presencaDoComprador: Number,
        @JacksonXmlProperty(localName = "procEmi")
        val processoDeEmissao: Number,
        @JacksonXmlProperty(localName = "verProc")
        val versaoAplicativoEmissao: String
)

data class Emitente(
        @JacksonXmlProperty(localName = "CNPJ")
        val cnpj: String,
        @JacksonXmlProperty(localName = "xNome")
        val nome: String,
        @JacksonXmlProperty(localName = "enderEmit")
        val endereco: Endereco,
        @JacksonXmlProperty(localName = "IE")
        val ie: String,
        @JacksonXmlProperty(localName = "IEST")
        val iest: String?,
        @JacksonXmlProperty(localName = "CRT")
        val crt: Number
)

data class Destinatario(
        @JacksonXmlProperty(localName = "CNPJ")
        val cnpj: String,
        @JacksonXmlProperty(localName = "xNome")
        val nome: String,
        @JacksonXmlProperty(localName = "enderDest")
        val endereco: Endereco,
        @JacksonXmlProperty(localName = "IE")
        val ie: String,
        @JacksonXmlProperty(localName = "email")
        val email: String
)

data class Endereco(
        @JacksonXmlProperty(localName = "xLgr")
        val logradouro: String,
        @JacksonXmlProperty(localName = "nro")
        val numero: String,
        @JacksonXmlProperty(localName = "xBairro")
        val bairro: String,
        @JacksonXmlProperty(localName = "cMun")
        val codigoMunicipio: Number,
        @JacksonXmlProperty(localName = "xMun")
        val municipio: String,
        @JacksonXmlProperty(localName = "UF")
        val uf: String,
        @JacksonXmlProperty(localName = "CEP")
        val cep: String,
        @JacksonXmlProperty(localName = "cPais")
        val codigoPais: Number,
        @JacksonXmlProperty(localName = "xPais")
        val pais: String,
        @JacksonXmlProperty(localName = "fone")
        val telefone: String
)

data class Item(
        @JacksonXmlProperty(localName = "nItem")
        val numeroItem: Number,
        @JacksonXmlProperty(localName = "prod")
        val produto: ProdutoNF,
        @JacksonXmlProperty(localName = "imposto")
        val imposto: Imposto
)

data class Imposto(
        @JacksonXmlProperty(localName = "ICMS")
        val icms: ICMS,
        @JacksonXmlProperty(localName = "IPI")
        val ipi: IPI,
        @JacksonXmlProperty(localName = "PIS")
        val pis: PIS,
        @JacksonXmlProperty(localName = "COFINS")
        val cofins: COFINS
)

data class ICMS(
        @JacksonXmlProperty(localName = "ICMS00")
        val icmS00: ICMS00
)

data class ICMS00(
        @JacksonXmlProperty(localName = "orig")
        val origem: Number,
        @JacksonXmlProperty(localName = "CST")
        val cst: String,
        @JacksonXmlProperty(localName = "modBC")
        val modBC: Number,
        @JacksonXmlProperty(localName = "vBC")
        val valorBC: Float,
        @JacksonXmlProperty(localName = "pICMS")
        val percentual: Float,
        @JacksonXmlProperty(localName = "vICMS")
        val valor: Float
)

data class IPI(
        @JacksonXmlProperty(localName = "cEnq")
        val codigoEnq: Number,
        @JacksonXmlProperty(localName = "IPITrib")
        val ipiTrib: IPITrib?
)

class IPITrib(
        @JacksonXmlProperty(localName = "CST")
        val cst: Number,
        @JacksonXmlProperty(localName = "vBC")
        val valorBC: Float,
        @JacksonXmlProperty(localName = "pIPI")
        val percentual: Float,
        @JacksonXmlProperty(localName = "vIPI")
        val valor: Float
)

data class PIS(
        @JacksonXmlProperty(localName = "PISAliq")
        val pisAliq: PISAliq
)

data class PISAliq(
        @JacksonXmlProperty(localName = "CST")
        val cst: Number,
        @JacksonXmlProperty(localName = "vBC")
        val valorBC: Float,
        @JacksonXmlProperty(localName = "pPIS")
        val percentual: Float,
        @JacksonXmlProperty(localName = "vPIS")
        val valor: Float
)

data class COFINS(
        @JacksonXmlProperty(localName = "COFINSAliq")
        val confisAliq: COFINSAliq
)

data class COFINSAliq(
        @JacksonXmlProperty(localName = "CST")
        val cst: Number,
        @JacksonXmlProperty(localName = "vBC")
        val valorBC: Float,
        @JacksonXmlProperty(localName = "pCOFINS")
        val percentual: Float,
        @JacksonXmlProperty(localName = "vCOFINS")
        val valor: Float
)

data class ProdutoNF(
        @JacksonXmlProperty(localName = "cProd")
        val codigo: String,
        @JacksonXmlProperty(localName = "cEAN")
        val codigoEAN: String,
        @JacksonXmlProperty(localName = "xProd")
        val descricao: String,
        @JacksonXmlProperty(localName = "NCM")
        val ncm: String,
        @JacksonXmlProperty(localName = "CEST")
        val cest: String,
        @JacksonXmlProperty(localName = "CFOP")
        val cfop: String,
        @JacksonXmlProperty(localName = "uCom")
        val unidade: String,
        @JacksonXmlProperty(localName = "vUnCom")
        val valorUnitario: Float,
        @JacksonXmlProperty(localName = "qCom")
        val quantidade: Number,
        @JacksonXmlProperty(localName = "vProd")
        val valorTotal: Float,
        @JacksonXmlProperty(localName = "cEANTrib")
        val codigoEANTrib: String,
        @JacksonXmlProperty(localName = "uTrib")
        val unidadeTrib: String,
        @JacksonXmlProperty(localName = "qTrib")
        val quantidadeTrib: Number,
        @JacksonXmlProperty(localName = "vUnTrib")
        val valorUnitarioTrib: Float,
        @JacksonXmlProperty(localName = "indTot")
        val indTot: Number,
        @JacksonXmlProperty(localName = "xPed")
        val numeroPedido: Long?
)

data class Totais(
        @JacksonXmlProperty(localName = "ICMSTot")
        val totaisIcms: TotaisIcms
)

data class TotaisIcms(
        @JacksonXmlProperty(localName = "vBC")
        val baseCalculoIcms: Float,
        @JacksonXmlProperty(localName = "vICMS")
        val valorICMS: Float,
        @JacksonXmlProperty(localName = "vICMSDeson")
        val valorIcmsDesoneracao: Float,
        @JacksonXmlProperty(localName = "vFCP")
        val valorFCP: Float,
        @JacksonXmlProperty(localName = "vBCST")
        val baseCalculoIcmsST: Float,
        @JacksonXmlProperty(localName = "vST")
        val valorST: Float,
        @JacksonXmlProperty(localName = "vFCPST")
        val valorFCPST: Float,
        @JacksonXmlProperty(localName = "vFCPSTRet")
        val valorFCPSTRet: Float,
        @JacksonXmlProperty(localName = "vProd")
        val valorProdutos: Float,
        @JacksonXmlProperty(localName = "vFrete")
        val valorFrete: Float,
        @JacksonXmlProperty(localName = "vSeg")
        val valorSeguro: Float,
        @JacksonXmlProperty(localName = "vDesc")
        val valorDesconto: Float,
        @JacksonXmlProperty(localName = "vII")
        val valorII: Float,
        @JacksonXmlProperty(localName = "vIPI")
        val valorIPI: Float,
        @JacksonXmlProperty(localName = "vIPIDevol")
        val valorIPIDevolucao: Float,
        @JacksonXmlProperty(localName = "vPIS")
        val valorPIS: Float,
        @JacksonXmlProperty(localName = "vCOFINS")
        val valorCOFINS: Float,
        @JacksonXmlProperty(localName = "vOutro")
        val valorOutro: Float,
        @JacksonXmlProperty(localName = "vNF")
        val valorTotalNF: Float
)

data class Transporte(
        @JacksonXmlProperty(localName = "modFrete")
        val modoFrete: Number,
        @JacksonXmlProperty(localName = "transporta")
        val transportadora: Transportadora,
        @JacksonXmlProperty(localName = "vol")
        val volume: Volume
)

data class Transportadora(
        @JacksonXmlProperty(localName = "CNPJ")
        val cnpj: String,
        @JacksonXmlProperty(localName = "xNome")
        val nome: String,
        @JacksonXmlProperty(localName = "IE")
        val ie: String,
        @JacksonXmlProperty(localName = "xEnder")
        val endereco: String,
        @JacksonXmlProperty(localName = "xMun")
        val municipio: String,
        @JacksonXmlProperty(localName = "UF")
        val UF: String
)

data class Volume(
        @JacksonXmlProperty(localName = "qVol")
        val quantidade: Number,
        @JacksonXmlProperty(localName = "esp")
        val especie: String,
        @JacksonXmlProperty(localName = "pesoL")
        val pesoLiquido: Float,
        @JacksonXmlProperty(localName = "pesoB")
        val pesoBruto: Float
)

data class Cobranca(
        @JacksonXmlProperty(localName = "fat")
        val fatura: Fatura,
        @JacksonXmlProperty(localName = "dup")
        @JacksonXmlElementWrapper(useWrapping = false)
        val dup: List<Duplicata>
)

data class Fatura(
        @JacksonXmlProperty(localName = "nFat")
        val numero: Number,
        @JacksonXmlProperty(localName = "vOrig")
        val valorOriginal: Float,
        @JacksonXmlProperty(localName = "vLiq")
        val valorLiquido: Float
)

data class Duplicata(
        @JacksonXmlProperty(localName = "nDup")
        val numero: String,
        @JacksonXmlProperty(localName = "dVenc")
        val dataVencimento: LocalDate,
        @JacksonXmlProperty(localName = "vDup")
        val valor: Float
)

data class Pagamento(
        @JacksonXmlProperty(localName = "detPag")
        val detPagamento: DetPagamento
)

data class DetPagamento(
        @JacksonXmlProperty(localName = "tPag")
        val tipo: Number,
        @JacksonXmlProperty(localName = "vPag")
        val valor: Float
)

data class InformacoesAdicionais(
        @JacksonXmlProperty(localName = "infCpl")
        val informacoesComplementares: String
)