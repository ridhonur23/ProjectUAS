import java.util.ArrayList;
import java.util.List;

class User {
    String username;
    String password;
    String role;
}

class Admin extends User {
    void tambahNasabah(String username, String password, Bank bank) {
        // Periksa apakah pengguna sudah ada
        for (User user : bank.users) {
            if (user.username.equals(username)) {
                System.out.println("Username sudah ada");
                return;
            }
        }

        // Jika pengguna belum ada, buat nasabah baru dan tambahkan ke daftar pengguna bank
        Nasabah nasabah = new Nasabah();
        nasabah.username = username;
        nasabah.password = password;
        nasabah.role = "nasabah";
        bank.users.add(nasabah);

        // Buat akun untuk nasabah baru
        Rekening rekening = new Rekening();
        rekening.nomorRekening = "REK" + (bank.rekenings.size() + 1);
        rekening.saldo = 0;
        rekening.riwayatTransaksi = new ArrayList<>();
        nasabah.nomorRekening = rekening.nomorRekening; // Tautkan nomor rekening ke nasabah
        bank.rekenings.add(rekening);

        System.out.println("Nasabah berhasil ditambahkan");
    }

    void lihatDetailNasabah(String username, Bank bank) {
        // Temukan nasabah
        for (User user : bank.users) {
            if (user.username.equals(username) && user.role.equals("nasabah")) {
                Nasabah nasabah = (Nasabah) user;

                // Tampilkan detail nasabah
                System.out.println("Username: " + nasabah.username);
                System.out.println("Role: " + nasabah.role);

                // Temukan dan tampilkan detail akun nasabah
                for (Rekening rekening : bank.rekenings) {
                    if (rekening.nomorRekening.equals(nasabah.nomorRekening)) {
                        System.out.println("Nomor Rekening: " + rekening.nomorRekening);
                        System.out.println("Saldo: " + rekening.saldo);
                        return;
                    }
                }
            }
        }
        System.out.println("Nasabah tidak ditemukan");
    }

    void lihatRiwayatTransaksi(String nomorRekening, Bank bank) {
        // Temukan akun
        for (Rekening rekening : bank.rekenings) {
            if (rekening.nomorRekening.equals(nomorRekening)) {
                // Tampilkan riwayat transaksi
                for (Transaksi transaksi : rekening.riwayatTransaksi) {
                    System.out.println("Jenis: " + transaksi.jenis);
                    System.out.println("Jumlah: " + transaksi.jumlah);
                    System.out.println();
                }
                return;
            }
        }
        System.out.println("Akun tidak ditemukan");
    }
}

class Nasabah extends User {
    String nomorRekening;

    void cekSaldo(Bank bank) {
        for (Rekening rekening : bank.rekenings) {
            if (rekening.nomorRekening.equals(this.nomorRekening)) {
                System.out.println("Saldo Anda: " + rekening.saldo);
                return;
            }
        }
        System.out.println("Akun tidak ditemukan");
    }

    void setor(Bank bank, double jumlah) {
        for (Rekening rekening : bank.rekenings) {
            if (rekening.nomorRekening.equals(this.nomorRekening)) {
                rekening.saldo += jumlah;
                System.out.println("Setoran berhasil");
                return;
            }
        }
        System.out.println("Akun tidak ditemukan");
    }

    void tarik(Bank bank, double jumlah) {
        for (Rekening rekening : bank.rekenings) {
            if (rekening.nomorRekening.equals(this.nomorRekening)) {
                if (rekening.saldo >= jumlah) {
                    rekening.saldo -= jumlah;
                    System.out.println("Penarikan berhasil");
                } else {
                    System.out.println("Saldo tidak mencukupi");
                }
                return;
            }
        }
        System.out.println("Akun tidak ditemukan");
    }

    void transfer(Bank bank, String keNomorRekening, double jumlah) {
        Rekening keRekening = null;
        Rekening dariRekening = null;

        for (Rekening rekening : bank.rekenings) {
            if (rekening.nomorRekening.equals(this.nomorRekening)) {
                dariRekening = rekening;
            } else if (rekening.nomorRekening.equals(keNomorRekening)) {
                keRekening = rekening;
            }
        }

        if (dariRekening != null && keRekening != null) {
            if (dariRekening.saldo >= jumlah) {
                dariRekening.saldo -= jumlah;
                keRekening.saldo += jumlah;
                System.out.println("Transfer berhasil");
            } else {
                System.out.println("Saldo tidak mencukupi");
            }
        } else {
            System.out.println("Akun tidak ditemukan");
        }
    }
}

class Rekening {
    String nomorRekening;
    double saldo;
    List<Transaksi> riwayatTransaksi;
}

class Bank {
    List<User> users;
    List<Rekening> rekenings;
}

class Transaksi {
    double jumlah;
    String jenis;
    Rekening rekening;
}

class AplikasiPerbankan {
    Bank bank;

    void login(String username, String password) {
        for (User user : bank.users) {
            if (user.username.equals(username) && user.password.equals(password)) {
                System.out.println("Login berhasil");
                return;
            }
        }
        System.out.println("Username atau password salah");
    }

    void registrasi(String username, String password, String peran) {
        // Periksa apakah pengguna sudah ada
        for (User user : bank.users) {
            if (user.username.equals(username)) {
                System.out.println("Username sudah ada");
                return;
            }
        }

        // Jika pengguna belum ada, buat pengguna baru dan tambahkan ke daftar pengguna bank
        if (peran.equals("admin")) {
            Admin admin = new Admin();
            admin.username = username;
            admin.password = password;
            admin.role = peran;
            bank.users.add(admin);
        } else if (peran.equals("nasabah")) {
            Nasabah nasabah = new Nasabah();
            nasabah.username = username;
            nasabah.password = password;
            nasabah.role = peran;
            bank.users.add(nasabah);
        } else {
            System.out.println("Peran tidak valid");
        }
    }

    void setorUang(String nomorRekening, double jumlah) {
        // Temukan akun
        for (Rekening rekening : bank.rekenings) {
            if (rekening.nomorRekening.equals(nomorRekening)) {
                // Perbarui saldo
                rekening.saldo += jumlah;

                // Catat transaksi
                Transaksi transaksi = new Transaksi();
                transaksi.jumlah = jumlah;
                transaksi.jenis = "setor";
                transaksi.rekening = rekening;
                rekening.riwayatTransaksi.add(transaksi);

                System.out.println("Setoran berhasil");
                return;
            }
        }
        System.out.println("Akun tidak ditemukan");
    }

    void tarikUang(String nomorRekening, double jumlah) {
        // Temukan akun
        for (Rekening rekening : bank.rekenings) {
            if (rekening.nomorRekening.equals(nomorRekening)) {
                // Periksa apakah cukup saldo
                if (rekening.saldo >= jumlah) {
                    // Perbarui saldo
                    rekening.saldo -= jumlah;

                    // Catat transaksi
                    Transaksi transaksi = new Transaksi();
                    transaksi.jumlah = jumlah;
                    transaksi.jenis = "tarik";
                    transaksi.rekening = rekening;
                    rekening.riwayatTransaksi.add(transaksi);

                    System.out.println("Penarikan berhasil");
                } else {
                    System.out.println("Saldo tidak mencukupi");
                }
                return;
            }
        }
        System.out.println("Akun tidak ditemukan");
    }

    void transferUang(String dariNomorRekening, String keNomorRekening, double jumlah) {
        Rekening dariRekening = null;
        Rekening keRekening = null;

        // Temukan akun sumber dan akun tujuan
        for (Rekening rekening : bank.rekenings) {
            if (rekening.nomorRekening.equals(dariNomorRekening)) {
                dariRekening = rekening;
            } else if (rekening.nomorRekening.equals(keNomorRekening)) {
                keRekening = rekening;
            }
        }

        if (dariRekening != null && keRekening != null) {
            // Periksa apakah cukup saldo di akun sumber
            if (dariRekening.saldo >= jumlah) {
                // Perbarui saldo
                dariRekening.saldo -= jumlah;
                keRekening.saldo += jumlah;

                // Catat transaksi dari akun sumber
                Transaksi transaksiDari = new Transaksi();
                transaksiDari.jumlah = jumlah;
                transaksiDari.jenis = "transfer";
                transaksiDari.rekening = dariRekening;
                dariRekening.riwayatTransaksi.add(transaksiDari);

                // Catat transaksi ke akun tujuan
                Transaksi transaksiKe = new Transaksi();
                transaksiKe.jumlah = jumlah;
                transaksiKe.jenis = "terima";
                transaksiKe.rekening = keRekening;
                keRekening.riwayatTransaksi.add(transaksiKe);

                System.out.println("Transfer berhasil");
            } else {
                System.out.println("Saldo tidak mencukupi");
            }
        } else {
            System.out.println("Akun tidak ditemukan");
        }
    }

    void tambahNasabah(String username, String password) {
        Admin admin = new Admin();
        admin.tambahNasabah(username, password, bank);
    }

    void lihatDetailNasabah(String username) {
        Admin admin = new Admin();
        admin.lihatDetailNasabah(username, bank);
    }

    void lihatRiwayatTransaksi(String nomorRekening) {
        Admin admin = new Admin();
        admin.lihatRiwayatTransaksi(nomorRekening, bank);
    }
}

public class MainProject {
    public static void main(String[] args) {
        AplikasiPerbankan aplikasiBank = new AplikasiPerbankan();
        aplikasiBank.bank = new Bank();
        aplikasiBank.bank.users = new ArrayList<>();
        aplikasiBank.bank.rekenings = new ArrayList<>();

        // Registrasi admin baru
        aplikasiBank.registrasi("admin1", "password1", "admin");

        // Admin login
        aplikasiBank.login("admin1", "password1");

        // Admin menambahkan nasabah baru
        aplikasiBank.tambahNasabah("nasabah1", "password2");

        // Nasabah login
        aplikasiBank.login("nasabah1", "password2");

        // Nasabah melakukan setoran
        aplikasiBank.setorUang("REK1", 1000);

        // Nasabah cek saldo
        Nasabah nasabah = (Nasabah) aplikasiBank.bank.users.get(1);
        nasabah.cekSaldo(aplikasiBank.bank);

        // Admin melihat detail nasabah
        aplikasiBank.lihatDetailNasabah("nasabah1");

        // Admin melihat riwayat transaksi
        aplikasiBank.lihatRiwayatTransaksi("REK1");
    }
}