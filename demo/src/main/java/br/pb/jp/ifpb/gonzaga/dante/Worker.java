package br.pb.jp.ifpb.gonzaga.dante;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Worker {
    public static void main(String[] args) throws Exception {
        System.out.println("## Worker ##");

        String NOME_FILA = "task_queue";

        //criando a fabrica de conexoes e criando uma conexao
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setUsername("mqadmin");
        connectionFactory.setPassword("Admin123XX_"); 
        Connection conexao = connectionFactory.newConnection();

        //criando um canal e declarando uma fila
        Channel canal = conexao.createChannel();
        boolean duravel = true;

        canal.queueDeclare(NOME_FILA, duravel, false, false, null);
        System.out.println ("[*] Aguardando mensagens. Para sair, pressione CTRL + C");

        int prefetchCount = 1;
        canal.basicQos(prefetchCount); // Aceita apenas uma mensagem unacked(sem ack) de cada vez
        //Definindo a funcao callback
        DeliverCallback callback = (consumerTag, delivery) -> {
            String mensagem = new String(delivery.getBody(), "UTF-8");
            System.out.println("Recebi a mensagem de Luiz Gonzaga de Lima Neto: " + mensagem);
            System.out.println ("[x] Recebido '" + mensagem + "'");
            try {
                doWork (mensagem);
            } finally {
                System.out.println ("[x] Feito");
                canal.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
        };

        boolean autoAck = false; // ack é feito aqui. Como está autoAck, enviará automaticamente

        //Consome da fila
        canal.basicConsume(NOME_FILA, autoAck, callback, consumerTag -> {});
    }

    private static void doWork(String task) {
        for (char ch: task.toCharArray ()) {
            if (ch == '.') {
                try {
                    Thread.sleep (1000);
                } catch (InterruptedException _ignored) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }   
}
