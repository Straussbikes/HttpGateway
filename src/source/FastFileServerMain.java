package source;

import java.io.IOException;
import java.util.Queue;

public class FastFileServerMain {

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            throw new IllegalArgumentException("insuficient arguments");
        }

        if (args[0].equals("FastFileSvr") && args.length < 4) {

            //Ver se args[1] e um IP e ver se args[2] é uma port valida
            if (args[1].matches(Constantes.IPV4Pattern) && args[2].matches(Constantes.PortPattern)) {
                System.out.println("Fast file server ");

                try {
                    // cria thread ffs e adiciona a pool de servers disponiveis pool nao ta a funcionar
                    FastFileSrv ffs = new FastFileSrv(args[1]);
                    ffs.start();

                } catch (IOException e) {
                    System.err.println("cannot start ffs");
                }
            }
        } else System.out.println("Parametros Invalidos!");
    }
}
