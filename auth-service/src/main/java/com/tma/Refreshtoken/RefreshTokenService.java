package com.tma.Refreshtoken;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    public void storeRefreshToken(String refreshToken){
        refreshTokenRepository.saveRefreshToken(refreshToken);
    }
    public boolean checkRefreshToken(String refreshToken){
        return refreshTokenRepository.checkExistRefreshToken(refreshToken);
    }
}
