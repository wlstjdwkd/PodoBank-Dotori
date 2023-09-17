import React,{useEffect, useState} from "react";
import { View, Text, Image, StyleSheet, TouchableOpacity } from "react-native";
import { AntDesign, Octicons  } from "@expo/vector-icons"; // AntDesign과 Entypo 아이콘 라이브러리 추가
import FooterScreen from "../Header/FooterScreen";
import AccessTokenRefreshModalScreen from "../Modal/AccessTokenRefreshModalScreen";
import { useDispatch, useSelector } from "react-redux";
import { userInformationInquiry, } from '../../apis/userapi'
import { accountListInquiry, } from '../../apis/accountapi'
import { setUserInfo } from '../../redux/slices/auth/user'

export default function HomeScreen({ navigation }) {
  const userTokenRefreshModalVisible = useSelector((state) => state.user.userTokenRefreshModalVisible)
  const accessToken = useSelector((state) => state.user.accessToken)
  const userInfo = useSelector((state) => state.user.userInfo)
  const [accountList, setAccountList] = useState([])

  const dispatch = useDispatch()
  //대표계좌 axios로 가져와야함

  //들어올 때 axios로 유저 정보 받아야함
  const hanldeUserInformationInquiry = async() =>{
    const response = await userInformationInquiry(accessToken)
    if(response.status===200){
      dispatch(setUserInfo(response.data))
    }else if(response.status===400){
      console.log('bad400 회원정보를 받아올 수 없습니다.')
    }else if(response.status===401){
      console.log('bad401 회원정보를 받아올 수 없습니다.')
    }else if(response.status===403){
      console.log('bad403 회원정보를 받아올 수 없습니다.')
    }else{
      console.log('오류발생: 회원정보를 받아올 수 없습니다.')
    }
  }

  // 계좌 목록 불러오는 함수
  const handleAccountListInquiry = async()=>{
    const response = await accountListInquiry(accessToken)
    if(response.status===200){
      console.log(userInfo.name,'님의 계좌 목록을 받아왔습니다.')
      setAccountList(response.data)
    }else if(response.status===400){
      console.log('bad400 조회실패로 계좌 목록을 받아올 수 없습니다.')
    }else if(response.status===401){
      console.log('bad401 권한 없음으로 계좌 목록을 받아올 수 없습니다.')
    }else{
      console.log('오류발생: 계좌 목록을 받아올 수 없습니다.')
    }
  }

  // 페이지가 로드될 때 hanldeUserInformationInquiry() 함수 실행
  useEffect(() => {
    hanldeUserInformationInquiry()
    handleAccountListInquiry()
  }, []);


  return (
    <View style={styles.container}>
      {/* 사용자 이름 */}
      <View style={styles.nameContainer}>
        {userInfo?
            (<Text style={styles.userNameBold}>{userInfo.name}</Text>)
            :(<Text style={styles.userNameBold}>정예진</Text>)
          }
        <Text style={styles.userName}>님</Text>
      </View>

      {/* 대표 계좌 */}
      <View style={styles.accountBox}>
        <TouchableOpacity
          style={styles.touchableOpacity}
          onPress={() => {
            navigation.navigate("AccountDetailScreen");
          }}
        >
          <View style={styles.bankInfo}>
            <View style={styles.bankLogoContainer}>
              <Image
                source={require("../../assets/images/logo_podo.png")}
                style={styles.bankLogo}
              />
            </View>
            <Text style={styles.bankName}>포도은행 통장</Text>
          </View>

          <Text style={styles.accountNumber}>1235-4568-4532</Text>
          <Text style={styles.balance}>1,000,000,000원</Text>
        </TouchableOpacity>

        <TouchableOpacity
          style={styles.transferButton}
          onPress={() => navigation.navigate("TransferScreen")}
        >
          <Text style={styles.buttonText}>이체</Text>
        </TouchableOpacity>
      </View>
      
      {/* 보유 계좌 목록 */}
      {accountList.map((account, index) => (
        <View key={index} style={styles.accountBox}>
          <TouchableOpacity
            style={styles.touchableOpacity}
            onPress={() => {
              navigation.navigate("AccountDetailScreen");
            }}
          >
            <View style={styles.bankInfo}>
              <View style={styles.bankLogoContainer}>
                <Image
                  source={require("../../assets/images/logo_podo.png")}
                  style={styles.bankLogo}
                />
              </View>
              <Text style={styles.bankName}>포도은행 통장</Text>
            </View>

            <Text style={styles.accountNumber}>{account.accountNumber}</Text>
            <Text style={styles.balance}>{account.balance}원</Text>
          </TouchableOpacity>

          <TouchableOpacity
            style={styles.transferButton}
            onPress={() => navigation.navigate("TransferScreen")}
          >
            <Text style={styles.buttonText}>이체</Text>
          </TouchableOpacity>
        </View>
      ))}


      {/* 계좌 추가 버튼 */}
      <TouchableOpacity
        style={styles.addAccountBox}
        onPress={() => {
          navigation.navigate("AccountConfigurationScreen");
        }}
      >
        <AntDesign name="pluscircleo" size={25} color="black" />
      </TouchableOpacity>

      {/* 설정 버튼 */}
      <View style={styles.settingsContainer}>
        <Octicons name="gear" size={16} color="black" />
        <Text style={styles.settingsText}>  대표계좌 설정</Text>
      </View>
      <FooterScreen navigation={navigation} />
      {userTokenRefreshModalVisible && <AccessTokenRefreshModalScreen navigation={navigation} />}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: "center",
    justifyContent: "flex-start",
    paddingHorizontal: 20,
    paddingTop: 100,
    backgroundColor: "white",
  },
  accountNumber: {
    fontSize: 15,
    marginLeft: 25,
    marginBottom: 10,
    alignSelf: "flex-start",
  },
  balance: {
    fontSize: 25,
    marginTop: 20,
    marginBottom: 30,
    marginLeft: 25,
    alignSelf: "flex-start",
  },
  buttonText: {
    color: "black",
    fontSize: 16,
    // fontWeight: "bold",
  },
  nameContainer: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "flex-start",
    width: "100%",
    marginBottom: 20,
  },
  userNameBold: {
    fontSize: 24,
    fontWeight: "bold",
  },
  userName: {
    fontSize: 24,
  },
  accountBox: {
    width: "100%",
    borderRadius: 10,
    padding: 20,
    backgroundColor: "white",
    marginBottom: 20,
    borderColor: "#757575",
    borderWidth: 1,
    // 그림자 스타일 추가
    elevation: 10,
    backgroundColor: "white",
  },
  bankInfo: {
    flexDirection: "row",
    alignItems: "center",
    marginBottom: 10,
  },
  bankLogoContainer: {
    width: 30, // 여기서 사이즈는 예시입니다. 원하는 사이즈로 조절해주세요.
    height: 30, // 여기서 사이즈는 예시입니다. 원하는 사이즈로 조절해주세요.
    marginRight: 5,
    alignItems: "center",
    justifyContent: "center",
    shadowColor: "#000",
    shadowOffset: {
      width: 0,
      height: 2,
    },
  },
  bankLogo: {
    width: 25,
    height: 25,
  },
  bankName: {
    fontSize: 16,
    // fontWeight: "bold",
  },
  transferButton: {
    marginTop: 20,
    backgroundColor: "#8B0FD750",
    padding: 10,
    borderRadius: 3,
    alignItems: "center",
    marginLeft: 25,
    marginRight: 25,
  },
  addAccountBox: {
    width: "100%",
    height: 60,
    borderRadius: 10,
    justifyContent: "center",
    alignItems: "center",
    marginBottom: 20,
    borderRadius: 10,
    borderColor: "#757575",
    borderWidth: 1,
    elevation: 10,
    backgroundColor: "white",
  },
  settingsContainer: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "center",
    width: "100%",
  },
  settingsIcon: {
    marginRight: 10,
  },
  settingsText: {
    fontSize: 16,
  },
});
