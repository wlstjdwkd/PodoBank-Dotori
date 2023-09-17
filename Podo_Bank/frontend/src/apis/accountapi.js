// src/api/accountApi.js
import axios from 'axios';
const apiAddress ="http://j9d107.p.ssafy.io:9000"

// 계좌가 최근에 보낸 계좌 3개를 받음
export const accountRecentTransfer = async (accountNumber, accessToken) => {
  try {
    const response = await axios.get(apiAddress+`/api/v1/account/${accountNumber}/recent`, null, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('최근 송금 계좌 조회 성공:', response.data);
    return response;
  } catch (error) {
    console.error('최근 송금 계좌 조회 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};
export const accountTransactionHistory = async (accountNumber, accessToken) => {
  try {
    const response = await axios.get(apiAddress+`/api/v1/account/${accountNumber}/history`, 
    {accountNumber:accountNumber,
      searchMonth:searchMonth,
      transactionType:transactionType,
      sortType:sortType,
      page:page,
    }, 
    {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('계좌 거래 내역 조회 성공:', response.data);
    return response;
  } catch (error) {
    console.error('계좌 거래 내역 조회 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};
export const accountTransactionDetail = async (accountNumber, accessToken) => {
  try {
    const response = await axios.get(apiAddress+`/api/v1/account/${accountNumber}/detail`, null, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('계좌 상세 조회 성공:', response.data);
    return response;
  } catch (error) {
    console.error('계좌 상세 조회 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};
export const accountOwnerInquiry = async (accountNumber) => {
  try {
    const response = await axios.get(apiAddress+`/api/v1/account/${accountNumber}`);
    console.log('계좌 소유주 조회 성공:', response.data);
    return response;
  } catch (error) {
    console.error('계좌 소유주 조회 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};
export const accountWithdrawal  = async (withdrawInfo, accessToken) => {
  try {
    const response = await axios.patch(apiAddress+'/api/v1/account/withdraw', withdrawInfo, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('계좌 출금 성공:', response.data);
    return response;
  } catch (error) {
    console.error('계좌 출금 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};
export const accountTypeInquiry = async () => {
  try {
    const response = await axios.get(apiAddress+'/api/v1/account/type');
    console.log('계좌 종류 조회 성공:', response.data);
    return response;
  } catch (error) {
    console.error('계좌 종류 조회 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};
export const accountTransfer = async (transferInfo, accessToken) => {
  try {
    const response = await axios.patch(apiAddress+'/api/v1/account/transfer', transferInfo, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('계좌 이체 성공:', response.data);
    return response;
  } catch (error) {
    console.error('계좌 이체 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};
export const accountPasswordInitialization = async (resetInfo, accessToken) => {
  try {
    const response = await axios.patch(apiAddress+'/api/v1/account/password/reset', resetInfo, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('계좌 비밀번호 초기화 성공:', response.data);
    return response;
  } catch (error) {
    console.error('계좌 비밀번호 초기화 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};
export const accountPasswordChange = async (changeInfo, accessToken) => {
  try {
    const response = await axios.patch(apiAddress+'/api/v1/account/password/change', changeInfo, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('계좌 비밀번호 변경 성공:', response.data);
    return response;
  } catch (error) {
    console.error('계좌 비밀번호 변경 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};
export const accountDeposit = async (sendInfo, accessToken) => {
  try {
    const response = await axios.patch(apiAddress+'/api/v1/account/deposit', sendInfo, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('계좌 입금 성공:', response.data);
    return response;
  } catch (error) {
    console.error('계좌 입금 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};
// 계좌 삭제하는데 다른거 안보내줘도 된다고??????????????
export const accountDelete = async (accessToken) => {
  try {
    const response = await axios.delete(apiAddress+'/api/v1/account/delete', null, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('계좌 삭제 성공:', response.data);
    return response;
  } catch (error) {
    console.error('계좌 삭제 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};
export const accountCreate = async (createInfo, accessToken) => {
  console.log(createInfo)
  try {
    const response = await axios.post(apiAddress+'/api/v1/account/create', createInfo, {
    // const response = await axios.post(apiAddress+'/api/v1/account/create', {accountType:createInfo.accountType, password:createInfo.password}, {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    })
    console.log('계좌 생성 성공:', response.data);
    return response;
  } catch (error) {
    console.error('계좌 생성 실패:', error);
    const response = error.response
    return response
    // throw error;
  }
};

export const accountListInquiry = async (accessToken) => {
  try {
    const response = await axios.get(apiAddress + '/api/v1/account/list', {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });
    console.log('계좌 목록 조회 성공:', response.data)
    return response;
  } catch (error) {
    console.error('계좌 목록 조회 실패:', error);
    const response = error.response
    return response
    
    // throw error;
  }
};