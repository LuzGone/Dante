package br.pb.jp.ifpb.gonzaga.dante;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

public class NewTask {
    public static void main(String[] args) throws Exception{
        //Criacao de uma factory de conexao, responsavel por criar as conexoes
        ConnectionFactory connectionFactory = new ConnectionFactory();

        //localizacao do gestor da fila (Queue Manager)
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("mqadmin");
        connectionFactory.setPassword("Admin123XX_"); 


        String NOME_FILA = "task_queue";
        boolean duravel = true;

        try(
                //criacao de uma coneccao
                Connection connection = connectionFactory.newConnection();
                //criando um canal na conexao
                Channel channel = connection.createChannel()) {
            //Esse corpo especifica o envio da mensagem para a fila

            //Declaracao da fila. Se nao existir ainda no queue manager, serah criada. Se jah existir, e foi criada com
            // os mesmos parametros, pega a referencia da fila. Se foi criada com parametros diferentes, lanca excecao
            channel.queueDeclare(NOME_FILA, duravel, false, false, null);
            String mensagem = String.join ("", args);
            //publica uma mensagem na fila
            channel.basicPublish ("", NOME_FILA, MessageProperties.PERSISTENT_TEXT_PLAIN, mensagem.getBytes ("UTF-8"));
            System.out.println ("[x] Enviado mensagem de Luiz Gonzaga de Lima Neto'" + mensagem + "'");
        }
    }
}
