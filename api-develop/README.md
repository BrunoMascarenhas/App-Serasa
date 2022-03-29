# Mercante API

Esta aplicação é utilizada para extender funcionalidades que não disponibilizadas pelo Winthor ou solucionar algum problema específico da Mercante. 


**1.  Envio de relatório da Amanco**
- Relatório semanal:
 > Todas as segundas-feiras as 6:00 é gerado o relatório com as vendas do período: 1º dia do mês até a última sexta feira da semana anterior.
- Relatório mensal:
> Todo segundo dia do mês as 6:05 é gerado um relatório com as vendas do mês anterior. 

**2.  Zerar limite de clientes com títulos a mais de 30 dias**
Todos os dias as 5:00 um job é disparado, que identifica os clientes que estão com sem pagar com mais de 30 dias de vencimento e zera o limite destes clientes.

  
 