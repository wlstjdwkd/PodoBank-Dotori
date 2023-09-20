import React, {useState} from "react";
import {
  View,
  Text,
  TextInput,
  StyleSheet,
  TouchableOpacity,
  Image,
  Alert,
  Modal,
} from "react-native";
import { AntDesign } from "@expo/vector-icons";
import { useSelector, useDispatch } from 'react-redux';
import {
  inputAccessToken, inputRefreshToken, setUserTokenRefreshModalVisible, 
  setAccessTokenExpiration, setIsnotReissuanceToken, setUserInfo
} from '../../redux/slices/auth/user'

import {userRefresh} from '../../apis/userapi'
import LoginScreen from "../Home/LoginScreen";

export default function AccessTokenRefreshModalScreen({ navigation }) {
  const accessToken = useSelector((state) => state.user.accessToken)
  const refreshToken = useSelector((state) => state.user.refreshToken)
  const dispatch = useDispatch();
  const userTokenRefreshModalVisible = useSelector((state) => state.user.userTokenRefreshModalVisible);
  // dispatch(setUserTokenRefreshModalVisible(false))  // 모달창 닫기에 사용됨
  // dispatch(setUserTokenRefreshModalVisible(!userTokenRefreshModalVisible))  // 모달창 닫기에 사용됨

  const handleUserRefresh = async()=>{
    const response = await userRefresh(refreshToken)
    if(response.status === 200){
      console.log('토큰 재발급 성공')
      dispatch(inputAccessToken(response.data.accessToken))
      dispatch(inputRefreshToken(response.data.refreshToken))
      dispatch(setUserTokenRefreshModalVisible(false))      
      dispatch(setAccessTokenExpiration(600))
    }else if(response.status === 400){
      console.log('토큰 재발급 실패')
    }else{
      console.log('오류 발생: 토큰 재발급')
    }
  }

  // const handleUserLogout = async() => {
  //   const response = await userLogout(accessToken)
  //   console.log('리스폰스',response.data)
  //   if(response.status===200){
  //     console.log('logout 성공')
  //     dispatch(inputAccessToken(null))
  //     dispatch(inputRefreshToken(null))
  //     dispatch(setAccessTokenExpiration(0))
  //     dispatch(setUserTokenRefreshModalVisible(false))
  //     dispatch(setIsnotReissuanceToken(false))
  //     navigation.reset({
  //       index: 0,
  //       routes: [{ name: 'LoginScreen',}],
  //     })
  //   }else if(response.status===400){
  //     console.log('logout 실패')
  //   }else if(response.status===401){
  //     console.log('logout 인증실패 실패')
  //   }else if(response.status===403){
  //     console.log('토큰없음')
  //   }else{
  //     console.log('오류발생 로그아웃 실패')
  //   }
  // }

  const handleNoButtonPress = () => {
    // handleUserLogout()
    // 아니오 버튼을 눌렀을 때 실행될 코드
    // accessToken과 refreshToken을 null로 만들고 LoginScreen로 이동
    dispatch(inputAccessToken(null));
    dispatch(inputRefreshToken(null));
    dispatch(setUserTokenRefreshModalVisible(false))
    dispatch(setIsnotReissuanceToken(true)) // 모달창을 닫고 재발급 안함을 true로 만들기;
    dispatch(setUserInfo(null))
    navigation.reset({
      index: 0,
      routes: [{ name: 'LoginScreen',}],
    })
  }

  return (
    <View style={styles.container}>
      {/* AccessToken 만료시 재발급 확인창 */}
      <View style={styles.centeredView}>
        <Modal
          animationType="none"//slide, fade가 있음
          transparent={true}
          visible={userTokenRefreshModalVisible}
          // onRequestClose={() => {
          //   Alert.alert('Modal has been closed.');
          //   // dispatch(setUserTokenRefreshModalVisible(!userTokenRefreshModalVisible));
          //   dispatch(setUserTokenRefreshModalVisible(false));
          // }}
        >
          <View style={styles.centeredView}>
            <View style={[styles.modalView]}>
              <Text style={styles.modalText}>로그인을 유지하시겠습니까?</Text>
              <View style={{
                flexDirection: 'row', 
                justifyContent: 'space-between',
                alignItems: 'center',
                marginHorizontal: 0,
              }}>
                <TouchableOpacity
                  style={[styles.button, styles.buttonClose, {flex: 1, marginRight: 5 }]}
                  onPress={() => {
                    handleUserRefresh()
                    // dispatch(setUserTokenRefreshModalVisible(!userTokenRefreshModalVisible))
                  }}>
                  <Text style={[styles.textStyle,]}>예</Text>
                </TouchableOpacity>
                <TouchableOpacity
                  style={[styles.button, styles.buttonClose, {flex: 1, marginLeft: 5 }]}
                  onPress={() => {
                    handleNoButtonPress()
                  }}>
                  <Text style={[styles.textStyle,]}>아니오</Text>
                </TouchableOpacity>
              </View>
            </View>
          </View>
        </Modal>
      </View>
    </View>
  );
}
const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: "center",
    paddingHorizontal: 20,
    backgroundColor: "transparent",
  },
  // 모달 창에 쓰이는 스타일
  centeredView: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    marginTop: 22,
  },
  modalView: {
    margin: 20,
    backgroundColor: 'white',
    borderRadius: 20,
    padding: 35,
    alignItems: 'center',
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 4,
    elevation: 5,
  },
  button: {
    borderRadius: 20,
    padding: 10,
    elevation: 2,
  },
  buttonOpen: {
    backgroundColor: '#F194FF',
  },
  buttonClose: {
    backgroundColor: '#2196F3',
  },
  textStyle: {
    color: 'white',
    fontWeight: 'bold',
    textAlign: 'center',
  },
  modalText: {
    marginBottom: 15,
    textAlign: 'center',
  },
});
