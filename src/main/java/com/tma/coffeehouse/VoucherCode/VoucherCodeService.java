package com.tma.coffeehouse.VoucherCode;

import com.example.demo.config.Notification;
import com.tma.coffeehouse.ExceptionHandling.CustomException;
import com.tma.coffeehouse.Product.Product;
import com.tma.coffeehouse.User.User;
import com.tma.coffeehouse.User.UserRepository;
import com.tma.coffeehouse.Utils.MessageQueueUtils;
import com.tma.coffeehouse.Voucher.Voucher;
import com.tma.coffeehouse.Voucher.VoucherRepository;
import com.tma.coffeehouse.Voucher.VoucherService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VoucherCodeService {
    private final VoucherCodeRepository voucherCodeRepository;
    private final VoucherRepository voucherRepository;
    private final UserRepository userRepository;
    private final MessageQueueUtils queueUtils;
    List<VoucherCode> generateVoucherCodes(Long voucherId, int number){
        Voucher voucher = voucherRepository.findById(voucherId).orElseThrow(
                () -> new CustomException("Không tìm thấy voucher có mã là " + voucherId, HttpStatus.NOT_FOUND)
        );
        List<VoucherCode> generatedCodes = new ArrayList();
        for(int i = 0 ;i < number; i++){
            VoucherCode newVoucherCode =
                    voucherCodeRepository.save
                            (new VoucherCode(null, voucher, UUID.randomUUID().toString(), null));
            generatedCodes.add(newVoucherCode);
        }
        return generatedCodes;
    }
    List<VoucherCode> getAll(Long voucherId){
        if (voucherId == null) return voucherCodeRepository.findAll();
        return voucherCodeRepository.findAllByVoucherId(voucherId);
    }

    @Transactional(rollbackOn = {Exception.class, Throwable.class})
    VoucherCode sendOneToUser(Long voucherId, Long userId){
        VoucherCode code = voucherCodeRepository.findOneByVoucherId(voucherId);
        User receiver =  userRepository.findById(userId).orElseThrow(
                () -> new CustomException("Không tìm thấy thông tin người khách này", HttpStatus.NOT_FOUND)
        );
        Voucher voucher = voucherRepository.findById(voucherId).orElseThrow(
                () -> new CustomException("Không tồn tại voucher này để tặng", HttpStatus.NOT_FOUND)
        );

        StringBuilder body = new StringBuilder("- Đơn hàng của bạn khi chứa những sản phẩm thuộc voucher sau sẽ được giảm. \n" +
                "- Voucher khi sử dụng sẽ giảm " + voucher.getPercentage() + " cho các sản phẩm bên dưới");
        for (Product product: voucher.getProducts()){
            body.append("   + ").append(product.getName()).append("\n");
        }
        String subject = "The Coffee House Gửi Tặng Bạn Voucher Giảm Giá!";
        queueUtils.pushEmailMessageQueue(subject, receiver.getEmail(),
                body.toString()
                );
        queueUtils.pushNotificationQueue(new Notification(subject, userId.toString(), body.toString(), ""));
        return code;
    }
}
