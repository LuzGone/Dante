package br.pb.jp.ifpb.gonzaga.dante;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Classe responsavel por enviar itens à fila
 */
public class Produtor {

    public static void main(String[] args) throws Exception{
        //Criacao de uma factory de conexao, responsavel por criar as conexoes
        ConnectionFactory connectionFactory = new ConnectionFactory();

        //localizacao do gestor da fila (Queue Manager)
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("mqadmin");
        connectionFactory.setPassword("Admin123XX_"); 


        String NOME_FILA = "tempoTotal";
        for(int i=1;i<=1000;i++){
            try(
                    //criacao de uma coneccao
                    Connection connection = connectionFactory.newConnection();
                    //criando um canal na conexao
                    Channel channel = connection.createChannel()) {
                //Esse corpo especifica o envio da mensagem para a fila

                //Declaracao da fila. Se nao existir ainda no queue manager, serah criada. Se jah existir, e foi criada com
                // os mesmos parametros, pega a referencia da fila. Se foi criada com parametros diferentes, lanca excecao
                channel.queueDeclare(NOME_FILA, false, false, false, null);
                
                String mensagem = ""+i+"-"+System.currentTimeMillis();
                //publica uma mensagem na fila
                channel.basicPublish("", NOME_FILA, null, mensagem.getBytes());
                if(i==1 || i==1000){
                    System.out.println("Enviei mensagem: " + mensagem);
                }
            }
        }
    }
}

