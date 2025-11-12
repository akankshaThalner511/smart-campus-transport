package com.cts.entity;

import com.cts.entity.enums.FeeStatus;
import com.cts.entity.enums.StudentStatus;
import com.cts.entity.enums.TransportValidation;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "student_transport_info")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentTransportInfo extends BaseAuditEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "student_id", updatable = false, nullable = false)
    private UUID studentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserAccount user;

    @Column(name = "roll_no")
    private String rollNo;

    @Column(name = "full_name")
    private String fullName; // ✅ Student Name field added

    private String department;

    @Enumerated(EnumType.STRING)
    private FeeStatus feeStatus;

    @Enumerated(EnumType.STRING)
    private TransportValidation transportValidation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stop_id")
    private BusStop stop;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "qr_code_url")
    private String qrCodeUrl;

    @Enumerated(EnumType.STRING)
    private StudentStatus status;

    public static final Map<String, StudentTransportInfo> DUMMY_DATA = new HashMap<>();

    static {
        DUMMY_DATA.put("101IT", StudentTransportInfo.builder()
                .studentId(UUID.randomUUID())
                .rollNo("101IT")
                .fullName("Amit Sharma") // ✅ Added name
                .department("IT")
                .feeStatus(FeeStatus.PAID)
                .transportValidation(TransportValidation.ALLOWED)
                .profileImageUrl("https://th.bing.com/th/id/OIP.bisQ1VHacWrzTNIgYfwE5AHaEK?w=290&h=180&c=7&r=0&o=7&cb=ucfimg2&dpr=1.3&pid=1.7&rm=3&ucfimg=1")
                .qrCodeUrl("/qr/101IT.png")
                .status(StudentStatus.ACTIVE)
                .build()
        );

        DUMMY_DATA.put("102CSE", StudentTransportInfo.builder()
                .studentId(UUID.randomUUID())
                .rollNo("102CSE")
                .fullName("Neha Singh") // ✅ Added name
                .department("CSE")
                .feeStatus(FeeStatus.UNPAID)
                .transportValidation(TransportValidation.DENIED)
                .profileImageUrl("https://img.freepik.com/premium-photo/young-woman-with-glasses-hair-tied-up-bun-is-standing-against-pink-background_1272475-1294.jpg")
                .qrCodeUrl("/qr/102CSE.png")
                .status(StudentStatus.ACTIVE)
                .build()
        );



        DUMMY_DATA.put("Aashu123", StudentTransportInfo.builder()
                .studentId(UUID.randomUUID())
                .rollNo("Aashu123")
                .fullName("Aashu123 Sharma") // ✅ Added name
                .department("IT")
                .feeStatus(FeeStatus.PAID)
                .transportValidation(TransportValidation.ALLOWED)
                .profileImageUrl("https://img.freepik.com/premium-photo/young-smiling-student-eyeglasses-denim-jacket-standing-with-paper-folders_176532-10688.jpg")
                .qrCodeUrl("/qr/Aashu123.png")
                .status(StudentStatus.ACTIVE)
                .build()
        );


        DUMMY_DATA.put("7Likhitha", StudentTransportInfo.builder()
                .studentId(UUID.randomUUID())
                .rollNo("7Likhitha")
                .fullName("7Likhitha Vallura") // ✅ Added name
                .department("IT")
                .feeStatus(FeeStatus.PAID)
                .transportValidation(TransportValidation.ALLOWED)
                .profileImageUrl("https://img.freepik.com/premium-photo/young-smiling-student-eyeglasses-denim-jacket-standing-with-paper-folders_176532-10688.jpg")
                .qrCodeUrl("/qr/7Likhitha.png")
                .status(StudentStatus.ACTIVE)
                .build()
        );

        DUMMY_DATA.put("15Manasa", StudentTransportInfo.builder()
                .studentId(UUID.randomUUID())
                .rollNo("15Manasa")
                .fullName("15Manasa Sharma") // ✅ Added name
                .department("IT")
                .feeStatus(FeeStatus.PAID)
                .transportValidation(TransportValidation.ALLOWED)
                .profileImageUrl("https://img.freepik.com/premium-photo/young-smiling-student-eyeglasses-denim-jacket-standing-with-paper-folders_176532-10688.jpg")
                .qrCodeUrl("/qr/15Manasa.png")
                .status(StudentStatus.ACTIVE)
                .build()
        );



    }
}
