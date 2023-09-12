// src/api/accountApi.js
import axios from 'axios';
const apiAddress ="192.168.100.181:8080"

// 계좌가 최근에 보낸 계좌 3개를 받음
export const accountRecentTransfer = async () => {
  try {
    const response = await axios.get(apiAddress+'/api/v1/account/{accountNumber}/recent');
    console.log(response.data)
    return response.data;
  } catch (error) {
    console.error('최근 송금 계좌 조회 실패:', error);
    // throw error;
  }
};
export const accountTransactionHistory = async () => {
  try {
    const response = await axios.get(apiAddress+'/api/v1/account/{accountNumber}/history');
    console.log(response.data)
    return response.data;
  } catch (error) {
    console.error('계좌 거래 내역 조회 실패:', error);
    // throw error;
  }
};
export const accountTransactionDetail = async () => {
  try {
    const response = await axios.get(apiAddress+'/api/v1/account/{accountNumber}/detail');
    console.log(response.data)
    return response.data;
  } catch (error) {
    console.error('계좌 상세 조회 실패:', error);
    // throw error;
  }
};
export const accountOwnerInquiry = async () => {
  try {
    const response = await axios.get(apiAddress+'/api/v1/account/{accountNumber}');
    console.log(response.data)
    return response.data;
  } catch (error) {
    console.error('계좌 소유주 조회 실패:', error);
    // throw error;
  }
};
export const accountWithdrawal  = async () => {
  try {
    const response = await axios.patch(apiAddress+'/api/v1/account/withdraw');
    console.log(response.data)
    return response.data;
  } catch (error) {
    console.error('계좌 출금 실패:', error);
    // throw error;
  }
};
export const accountTypeInquiry = async () => {
  try {
    const response = await axios.get(apiAddress+'/api/v1/account/type');
    console.log(response.data)
    return response.data;
  } catch (error) {
    console.error('계좌 종류 조회 실패:', error);
    // throw error;
  }
};
export const accountTransfer = async () => {
  try {
    const response = await axios.patch(apiAddress+'/api/v1/account/transfer');
    console.log(response.data)
    return response.data;
  } catch (error) {
    console.error('계좌 이체 실패:', error);
    // throw error;
  }
};
export const accountPasswordInitialization = async () => {
  try {
    const response = await axios.patch(apiAddress+'/api/v1/account/password/reset');
    console.log(response.data)
    return response.data;
  } catch (error) {
    console.error('계좌 비밀번호 초기화 실패:', error);
    // throw error;
  }
};
export const accountPasswordChange = async () => {
  try {
    const response = await axios.patch(apiAddress+'/api/v1/account/password/change');
    console.log(response.data)
    return response.data;
  } catch (error) {
    console.error('계좌 비밀번호 변경 실패:', error);
    // throw error;
  }
};
export const accountDeposit = async () => {
  try {
    const response = await axios.patch(apiAddress+'/api/v1/account/deposit');
    console.log(response.data)
    return response.data;
  } catch (error) {
    console.error('계좌 입금 실패:', error);
    // throw error;
  }
};
export const accountDelete = async () => {
  try {
    const response = await axios.delete(apiAddress+'/api/v1/account');
    console.log(response.data)
    return response.data;
  } catch (error) {
    console.error('계좌 삭제 실패:', error);
    // throw error;
  }
};
export const accountCreate = async () => {
  try {
    const response = await axios.post(apiAddress+'/api/v1/account');
    console.log(response.data)
    return response.data;
  } catch (error) {
    console.error('계좌 생성 실패:', error);
    // throw error;
  }
};
export const accountListInquiry = async () => {
  try {
    const response = await axios.get(apiAddress+'/api/v1/account');
    console.log(response.data)
    return response.data;
  } catch (error) {
    console.error('계좌 목록 조회 실패:', error);
    // throw error;
  }
};