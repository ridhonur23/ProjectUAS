import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// Kelas untuk merepresentasikan transaksi
class Transaksi {
    private String pengirim;
    private String penerima;
    private double jumlah;

    public Transaksi(String pengirim, String penerima, double jumlah) {
        this.pengirim = pengirim;
        this.penerima = penerima;
        this.jumlah = jumlah;
    }

    public String getPengirim() {
        return pengirim;
    }

    public String getPenerima() {
        return penerima;
    }

    public double getJumlah() {
        return jumlah;
    }
}

// Kelas untuk merepresentasikan nasabah
class Nasabah {
    private String username;
    private String password;
    private double saldo;
    private List<Transaksi> riwayatTransaksi;

    public Nasabah(String username, String password) {
        this.username = username;
        this.password = password;
        this.saldo = 0.0;
        this.riwayatTransaksi = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public double getSaldo() {
        return saldo;
    }

    public List<Transaksi> getRiwayatTransaksi() {
        return riwayatTransaksi;
    }

    public void setor(double jumlah) {
        saldo += jumlah;
        riwayatTransaksi.add(new Transaksi(username, "Setor", jumlah));
    }

    public void transfer(Nasabah penerima, double jumlah) {
        if (saldo >= jumlah) {
            saldo -= jumlah;
            penerima.setor(jumlah);
            riwayatTransaksi.add(new Transaksi(username, penerima.getUsername(), jumlah));
        } else {
            System.out.println("Saldo tidak mencukupi untuk transfer.");
        }
    }
}

// Kelas untuk merepresentasikan sistem pencatatan uang
class SistemPencatatanUang {
    public Map<String, Nasabah> nasabahMap;
    public Nasabah penggunaMasuk;

    public SistemPencatatanUang() {
        this.nasabahMap = new HashMap<>();
        nasabahMap.put("admin", new Nasabah("admin", "adminpass"));
        nasabahMap.put("user1", new Nasabah("user1", "userpass"));
        nasabahMap.put("user2", new Nasabah("user2", "userpass"));
    }

    public boolean login(String username, String password) {
        if (nasabahMap.containsKey(username) && nasabahMap.get(username).getPassword().equals(password)) {
            penggunaMasuk = nasabahMap.get(username);
            return true;
        } else {
            return false;
        }
    }

    public void logout() {
        penggunaMasuk = null;
    }

    public void tampilkanSaldo() {
        System.out.println("Saldo: $" + penggunaMasuk.getSaldo());
    }

    public void tampilkanRiwayatTransaksi() {
        List<Transaksi> transaksiList = penggunaMasuk.getRiwayatTransaksi();
        System.out.println("Riwayat Transaksi untuk " + penggunaMasuk.getUsername() + ":");
        for (Transaksi transaksi : transaksiList) {
            System.out.println("Dari: " + transaksi.getPengirim() +
                    ", Ke: " + transaksi.getPenerima() +
                    ", Jumlah: $" + transaksi.getJumlah());
        }
    }

    public void tampilkanSemuaNasabah() {
        if (penggunaMasuk.getUsername().equals("admin")) {
            System.out.println("Daftar Semua Nasabah:");
            for (Nasabah nasabah : nasabahMap.values()) {
                System.out.println("Username: " + nasabah.getUsername() + ", Saldo: $" + nasabah.getSaldo());
            }
        } else {
            System.out.println("Akses ditolak. Akses admin diperlukan.");
        }
    }

    public void tambahNasabah(String username, String password) {
        if (penggunaMasuk.getUsername().equals("admin")) {
            nasabahMap.put(username, new Nasabah(username, password));
            System.out.println("Nasabah " + username + " ditambahkan dengan sukses.");
        } else {
            System.out.println("Akses ditolak. Akses admin diperlukan.");
        }
    }
}

// Kelas utama untuk menjalankan program
public class MainProject {
    public static void main(String[] args) {
        SistemPencatatanUang sistemPencatatanUang = new SistemPencatatanUang();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Selamat datang di Sistem Pencatatan Uang");
            System.out.print("Masukkan username: ");
            String username = scanner.nextLine();
            System.out.print("Masukkan password: ");
            String password = scanner.nextLine();

            if (sistemPencatatanUang.login(username, password)) {
                while (true) {
                    System.out.println("\n1. Setor");
                    System.out.println("2. Transfer");
                    System.out.println("3. Lihat Saldo");
                    System.out.println("4. Lihat Riwayat Transaksi");
                    System.out.println("5. Logout");

                    if (username.equals("admin")) {
                        System.out.println("6. Lihat Semua Nasabah");
                        System.out.println("7. Tambah Nasabah");
                    }

                    System.out.print("Pilih opsi: ");
                    int pilihan = Integer.parseInt(scanner.nextLine());

                    switch (pilihan) {
                        case 1:
                            System.out.print("Masukkan jumlah setoran: $");
                            double jumlahSetoran = Double.parseDouble(scanner.nextLine());
                            sistemPencatatanUang.penggunaMasuk.setor(jumlahSetoran);
                            break;
                        case 2:
                            System.out.print("Masukkan username penerima: ");
                            String usernamePenerima = scanner.nextLine();
                            System.out.print("Masukkan jumlah transfer: $");
                            double jumlahTransfer = Double.parseDouble(scanner.nextLine());
                            if (sistemPencatatanUang.nasabahMap.containsKey(usernamePenerima)) {
                                Nasabah penerima = sistemPencatatanUang.nasabahMap.get(usernamePenerima);
                                if (sistemPencatatanUang.penggunaMasuk.getSaldo() >= jumlahTransfer) {
                                    sistemPencatatanUang.penggunaMasuk.transfer(penerima, jumlahTransfer);
                                } else {
                                    System.out.println("Saldo tidak mencukupi untuk transfer.");
                                }
                            } else {
                                System.out.println("Penerima tidak ditemukan.");
                            }
                            break;
                        case 3:
                            sistemPencatatanUang.tampilkanSaldo();
                            break;
                        case 4:
                            sistemPencatatanUang.tampilkanRiwayatTransaksi();
                            break;
                        case 5:
                            sistemPencatatanUang.logout();
                            break;
                        case 6:
                            if (username.equals("admin")) {
                                sistemPencatatanUang.tampilkanSemuaNasabah();
                            }
                            break;
                        case 7:
                            if (username.equals("admin")) {
                                System.out.print("Masukkan username nasabah baru: ");
                                String usernameBaru = scanner.nextLine();
                                System.out.print("Masukkan password nasabah baru: ");
                                String passwordBaru = scanner.nextLine();
                                sistemPencatatanUang.tambahNasabah(usernameBaru, passwordBaru);
                            }
                            break;
                        default:
                            System.out.println("Pilihan tidak valid. Silakan coba lagi.");
                    }

                    if (pilihan == 5) {
                        break;
                    }
                }
            } else {
                System.out.println("Username atau password tidak valid. Silakan coba lagi.");
            }
        }
    }
}
