package br.com.mercante.api.scheduling

import br.com.mercante.api.service.ClienteService
//import br.com.mercante.api.service.FornecedorService
import br.com.mercante.api.service.SerasaService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
@EnableScheduling
class SchedulingConfig(//private val fornecedorService: FornecedorService,
                       private val clienteService: ClienteService,
                       private val serasaService: SerasaService) {

    /**@Scheduled(cron = "0 0 6 * * 1")
    fun scheduleRelatorioAmancoSemanal() {
        log.debug("Iniciando tarefa agendada scheduleRelatorioAmancoSemanal() ${LocalDateTime.now()}")
        this.fornecedorService.gerarRelatorio('S')
    }

    @Scheduled(cron = "0 5 6 1 * *")
    fun scheduleRelatorioAmancoMensal() {
        log.debug("Iniciando tarefa agendada scheduleRelatorioAmancoMensal() ${LocalDateTime.now()}")
        this.fornecedorService.gerarRelatorio('M')
    }**/

    @Scheduled(cron = "0 0 5 * * *")
    fun scheduleZerarLimitesDeClientes() {
        log.debug("Iniciando tarefa agendada scheduleZerarLimitesDeClientes() ${LocalDateTime.now()}")
        this.clienteService.bloquearClientesComLimiteVencido()
    }

   /** @Scheduled(cron = "0 30 7 * * *")
    fun scheduleProcessarValidacaoClienteSefaz() {
        log.debug("Iniciando tarefa agendada scheduleProcessarValidacaoClienteSefaz() ${LocalDateTime.now()}")
        this.clienteService.processarValidacaoSefaz()
    }**/

    @Scheduled(cron = "0 30 6 * * *")
    fun scheduleProcessarSerasaPefin() {
        log.debug("Iniciando tarefa agendada scheduleProcessarValidacaoClienteSefaz() ${LocalDateTime.now()}")
        this.serasaService.processarSerasaPefin()
    }

    companion object {
        private val log = LoggerFactory.getLogger(this::class.java)
    }
}