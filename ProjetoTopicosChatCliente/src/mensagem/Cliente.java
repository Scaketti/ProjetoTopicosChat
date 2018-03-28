/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mensagem;

import gui.TelaCliente;
import java.rmi.Naming;
import sun.rmi.registry.RegistryImpl;

/**
 *
 * @author Scaketti
 */
public class Cliente {

    private String nome;
    private String apelido;
    private String ip;
    private int porta;
    private String logMensagens;
    private boolean conectado;

    public Cliente(TelaCliente tCliente) {
        nome = "";
        apelido = "";
        ip = "localhost";
        porta = 0;
        logMensagens = "";
        conectado = false;
    }

    /**
     * @return the nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * @return the apelido
     */
    public String getApelido() {
        return apelido;
    }

    /**
     * @param apelido the apelido to set
     */
    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    /**
     * @return the ip
     */
    public String getIp() {
        return ip;
    }

    /**
     * @param ip the ip to set
     */
    public void setIp(String ip) {
        this.ip = ip;
    }

    /**
     * @return the porta
     */
    public int getPorta() {
        return porta;
    }

    /**
     * @param porta the porta to set
     */
    public void setPorta(int porta) {
        this.porta = porta;
    }

    /**
     * @return the logMensagens
     */
    public String getLogMensagens() {
        return logMensagens;
    }

    /**
     * @param logMensagens the logMensagens to set
     */
    public void setLogMensagens(String logMensagens) {
        this.logMensagens = logMensagens;
    }

    public void registraCliente(TelaCliente tCliente) {
        try {
            //Registrando o serviço em uma determinada porta.
            RegistryImpl registryImpl = new RegistryImpl(porta);

            //Instanciando a classe ServidorImpl que é do tipo ServidorInterface.
            ClienteChatInterface cliente = new ClienteImpl(tCliente);

            //Registra o Servidor
            Naming.rebind("rmi://" + ip + ":" + porta + "/chat", cliente);

        } catch (Exception e) {
            System.out.println("Erro : Mensagem : " + e.getMessage());
        }
    }

    /**
     * @return the conectado
     */
    public boolean isConectado() {
        return conectado;
    }

    /**
     * @param conectado the conectado to set
     */
    public void setConectado(boolean conectado) {
        this.conectado = conectado;
    }

}
