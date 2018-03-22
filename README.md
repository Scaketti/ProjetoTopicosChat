# DESCRIÇÃO

* Implementar uma solução que contenha um servidor responsável por intermediar
a troca de mensagens entre clientes.

* O sistema deverá obrigatoriamente ser implementado em 2 projetos distintos.
Sendo um o Cliente e outro o Servidor.

* As mensagens trocadas entre Cliente e Sevidor deverão ser criptografadas:	

	* Utilizem a classe nativa Cipher.

Considerar os seguintes requisitos:

* Servidor: será responsável por intermediar a troca de mensagens entre dois ou
mais clientes. Para isso, o mesmo deve possibilitar a conexão e desconexão de
clientes.

	* Método de conexão: Deve receber os argumentos: apelido do cliente, nome
do cliente, IP do cliente e porta do cliente. Este método retornará 0 caso o
cliente conseguiu realizar a conexão, caso contrário, retornará 1.
o Método de desconexão: deve receber como argumentos: apelido do cliente,
IP do cliente e porta do cliente.

	* Método de encaminhamento de mensagens: deve receber os argumentos:
apelido de origem, apelido de destino e mensagem. Caso o apelido de destino
for “TODOS”, a mensagem deverá ser enviada a todos os clientes conectados
ao servidor, caso contrário, a mensagem será enviada apenas ao cliente
informado. Este método retornará 0 caso a mensagem seja enviada com
sucesso, caso contrário, retornará 1.

* Interface Gráfica:
	* Possibilidade do usuário informar a porta que deseja disponibilizar o
serviço;
	* Componente que ilustre todos os clientes que estão conectados ao
servidor;
	* Componente que apresente em tempo real o relatório de novas
conexões/desconexões de cliente, bem como o envio de mensagens.

* Cliente: deve ser capaz de conectar a um servidor para enviar e receber
mensagens de outros clientes que estejam conectados ao mesmo servidor.
	* Método de nova conexão: deve receber os argumentos: apelido e nome do
cliente que se conectou ao servidor.
	* Método de desconexão: deve receber os argumentos: apelido e nome do
cliente que se desconectou do servidor.
	* Método de recebimento de mensagem: deve receber os argumentos: apelido
de origem e a mensagem.
	* Interface Gráfica:
		* Possibilidade do usuário informar o IP e Porta do servidor que deseja trocar
informações;
		* Possibilidade do usuário escolher a sua própria porta;
		* Possibilidade do usuário informar seu nome e seu Apelido;
		* Componente para ilustrar as mensagens que estão sendo enviadas e
recebidas;
		* Componente que ilustra todos os clientes que estão conectados a este
mesmo servidor;
		* Possibilidade de enviar mensagem a todos os outros clientes conectados
ao mesmo servidor.
