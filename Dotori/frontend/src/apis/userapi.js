// 유저 정보와 관련된 API들을 모아둠 users, reward

import axios from 'axios';
// apiAddress는 수정 필요
const apiAddress ="http://j9d107.p.ssafy.io:9100"

// users
// // 사용자 회원탈퇴
// export const userWithdraw = async (withdrawData, accessToken) => {
//   try {
//     const response = await axios.delete(apiAddress+`/v1/user`, withdrawData, {
//       headers: {
//         Authorization: `Bearer ${accessToken}`,
//       },
//     });
//     console.log('사용자 회원탈퇴 성공:', response.data);
//     return response;
//   } catch (error) {
  //     console.error('사용자 회원탈퇴 실패:', error);
  //     const response = error.response
//     return response
//     // throw error;
//   }
// };

// 사용자 정보 조회
export const userInfoInquiry = async (infoInquiryData, accessToken) => {
  try {
    const response = await axios.get(apiAddress+`/v1/user`, infoInquiryData, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('사용자 정보 조회 성공:', response.data);
    return response;
  } catch (error) {
    console.error('사용자 정보 조회 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};
// 사용자 회원가입
export const userSignup = async (signupData) => {
  signupData.role = "ROLE_USER";
  console.log(signupData)
  try {
    const response = await axios.put(apiAddress+`/v1/user/signup`, signupData);
    console.log('사용자 회원가입 성공:', response.data);
    return response;
  } catch (error) {
    console.error('사용자 회원가입 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};
// 사용자 로그인
export const userLogin = async (loginData) => {
  try {
    const response = await axios.post(apiAddress+`/v1/user/signin`, loginData);
    console.log('사용자 로그인 성공:', response.data);
    return response;
  } catch (error) {
    console.error('사용자 로그인 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};
// 사용자 로그아웃
export const userLogout = async (accessToken) => {
  try {
    const response = await axios.post(apiAddress+`/v1/user/logout`, null, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('사용자 로그아웃 성공:', response.data);
    return response;
  } catch (error) {
    console.error('사용자 로그아웃 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};
// 사용자 비밀번호 변경
export const userPasswordChange = async (pwChangeData, accessToken) => {
  try {
    const response = await axios.patch(apiAddress+`/v1/user/password`, pwChangeData, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('사용자 비밀번호 변경 성공:', response.data);
    return response;
  } catch (error) {
    console.error('사용자 비밀번호 변경 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};
// 사용자 휴대전화 번호 변경
export const userCellPhoneNumberChange = async (cellPhoneNumberChangeData, accessToken) => {
  try {
    const response = await axios.patch(apiAddress+`/v1/user/phoneNumber`, cellPhoneNumberChangeData, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('사용자 휴대전화 번호 변경 성공:', response.data);
    return response;
  } catch (error) {
    console.error('사용자 휴대전화 번호 변경 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};
// 사용자 생년월일 변경
export const userBirthdateChange = async (birthdateChangeData, accessToken) => {
  try {
    const response = await axios.patch(apiAddress+`/v1/user/birthDate`, birthdateChangeData, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('사용자 생년월일 변경 성공:', response.data);
    return response;
  } catch (error) {
    console.error('사용자 생년월일 변경 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};
// 사용자 탈퇴하기
export const userExitDotori = async (exitDotoriData, accessToken) => {
  try {
    const response = await axios.patch(apiAddress+`/v1/user/retire`, exitDotoriData, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('사용자 탈퇴하기 성공:', response.data);
    return response;
  } catch (error) {
    console.error('사용자 탈퇴하기 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};
// 사용자 아이디 중복 검사
export const userIdDuplicatedCheck = async (idDuplicatedCheckData) => {
  try {
    // const response = await axios.get(apiAddress+`/v1/user/${idDuplicatedCheckData.id}`, idDuplicatedCheckData);
    const response = await axios.get(apiAddress+`/v1/user/${idDuplicatedCheckData}`);
    console.log('사용자 아이디 중복 검사 성공:', response.data);
    return response;
  } catch (error) {
    console.error('사용자 아이디 중복 검사 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};
// 사용자 이메일 인증코드 전송
export const userSendEmail = async (email) => {
  try {
    // const response = await axios.get(apiAddress+`/v1/user/${idDuplicatedCheckData.id}`, idDuplicatedCheckData);
    const response = await axios.post(apiAddress+`/v1/user/email/check-id`, {id:email});
    console.log('사용자 이메일 인증코드 전송 성공:', response.data);
    return response;
  } catch (error) {
    console.error('사용자 이메일 인증코드 전송 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};
// 사용자 이메일 인증코드 검증
export const userEmailCodeVerificate = async (emailVerificationCode) => {
  try {
    // const response = await axios.get(apiAddress+`/v1/user/${idDuplicatedCheckData.id}`, idDuplicatedCheckData);
    const response = await axios.post(apiAddress+`/v1/user/email/check-code`, {code:emailVerificationCode});
    console.log('사용자 이메일 인증코드 검증 성공:', response.data);
    return response;
  } catch (error) {
    console.error('사용자 이메일 인증코드 검증 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};


// reward
// 사용자 현재 도토리 갯수 조회
export const userDotoriValueCheck = async (dotoriValueCheckData, accessToken) => {
  try {
    const response = await axios.get(apiAddress+`/v1/reward/dotori`, dotoriValueCheckData, {
      headers: {
          Authorization: `Bearer ${accessToken}`,
        },
    });
    console.log('사용자 현재 도토리 갯수 조회 성공:', response.data);
    return response;
  } catch (error) {
    console.error('사용자 현재 도토리 갯수 조회 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};
// 사용자 현재 코인 갯수 조회
export const userCoinValueCheck = async (coinValueCheckData, accessToken) => {
  try {
    const response = await axios.get(apiAddress+`/v1/reward/coin`, coinValueCheckData, {
      headers: {
          Authorization: `Bearer ${accessToken}`,
        },
    });
    console.log('사용자 현재 코인 갯수 조회 성공:', response.data);
    return response;
  } catch (error) {
    console.error('사용자 현재 코인 갯수 조회 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};
// 사용자 도토리를 코인전환
export const userChangeDotoriToCoin = async (changeDotoriToCoinData, accessToken) => {
  try {
    const response = await axios.patch(apiAddress+`/v1/reward`, changeDotoriToCoinData, {
      headers: {
          Authorization: `Bearer ${accessToken}`,
        },
    });
    console.log('사용자 도토리를 코인전환 성공:', response.data);
    return response;
  } catch (error) {
    console.error('사용자 도토리를 코인전환 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};
