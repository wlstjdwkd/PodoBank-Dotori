import React, { useEffect, useState } from "react";
import {
  View,
  Text,
  Image,
  TouchableOpacity,
  StyleSheet,
  FlatList,
  ScrollView,
} from "react-native";

import FooterScreen from "../Components/FooterScreen";
import { useDispatch, useSelector } from "react-redux";
import {accountWholeInquiry} from "../../apis/accountapi"
import {userInfoInquiry} from '../../apis/userapi'

const banks = [
  {
    id: "1",
    name: "월급 통장",
    balance: 10000,
  },
  {
    id: "2",
    name: "비상금 통장",
    balance: 20000,
  },
  // {
  //   id: "3",
  //   name: "비상금 통장",
  //   balance: "20,000원",
  // },
  // {
  //   id: "4",
  //   name: "비상금 통장",
  //   balance: "20,000원",
  // },
  // {
  //   id: "5",
  //   name: "비상금 통장",
  //   balance: "20,000원",
  // },
  // ... 다른 은행들의 데이터
];
const formatNumber = (num) => {
  return num.toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");
};


export default function MainPageScreen({ navigation }) {
  // 토큰
  const grantType =  useSelector((state)=>state.user.grantType)
  const accessToken =  useSelector((state)=>state.user.accessToken)
  const refreshToken =  useSelector((state)=>state.user.refreshToken)
  const dispatch = useDispatch()
  // 그 외
  const [accountList, setAccountList] = useState([])
  const [userInfo, setUserInfo] = useState(null)


  // // 정보조회 함수
  // const do정보조회 = async () => {
  //   const response = await 함수()
  //   if(response.status === 200){
  //     console.log('연결 계좌 조회 성공')
  //   }else if(response.status === 400){
  //     console.log('연결 계좌 조회 실패')
  //   }
  // }

  const doAccountWholeInquiry = async () =>{
    try{
      const response = await accountWholeInquiry(accessToken, grantType)
      if(response.status === 200){
        setAccountList(response.data)
      }else{
        console.log("전체 계좌 리스트 불러오기 실패")
      }
    }catch(error){
      console.log(error)
    }
  }
  const doUserInfoInquiry = async () => {
    try {
      const response = await userInfoInquiry(accessToken, grantType);
      if(response.status===200){
        setUserInfo(response.data);
      }else{
        console.log('사용자 정보 조회 실패',response.status)
      }
    } catch (error) {
      console.error('오류 발생 : 사용자 정보 조회 실패:', error);
    }
  }

  useEffect(()=>{
    // do정보조회()
    doAccountWholeInquiry()
    doUserInfoInquiry()
  },[])

  return (
    <View style={styles.container}>
      <View style={styles.innerContainer}>
        <FlatList
          data={banks}
          showsVerticalScrollIndicator={false} // 수직 스크롤바 숨김
          ListHeaderComponent={
            <>
              <View style={styles.header}>
                <View></View>
                <Image
                  style={styles.logo}
                  source={require("../../assets/images/dotori_logo.png")}
                />
                <TouchableOpacity style={styles.helpButton}>
                  <Text style={styles.helpIcon}>?</Text>
                </TouchableOpacity>
              </View>

              <Text style={styles.title}>당신의 소비를 계획 해보세요!</Text>
              <Text style={styles.subtitle}>
                계좌를 등록하고 계획을 만들어 볼까요?
              </Text>

              <Image
                style={styles.rightImage}
                source={require("../../assets/images/Hamster/MainHamster.png")}
              />
            </>
          }
          renderItem={({ item }) => (
            <TouchableOpacity
              style={styles.bankContainer}
              onPress={() => navigation.navigate("PlanMainScreen")}
            >
              <View style={styles.imageText}>
                <Image
                  style={styles.bankIcon}
                  source={require("../../assets/images/logo_podo.png")}
                />
                <Text style={styles.bankName}>{item.name}</Text>
              </View>

              <View style={styles.bankTextContainer}>
                <View style={styles.balanceRow}>
                  <Text style={styles.bankSubtitle}>잔액</Text>
                  <Text style={styles.bankBalance}>
                    {formatNumber(item.balance)}원
                  </Text>
                </View>
              </View>
            </TouchableOpacity>
          )}
          ListFooterComponent={
            <>
              <TouchableOpacity
                style={styles.addButton}
                onPress={() => {
                  navigation.navigate("OneCent1Screen", {userName:userInfo.userName})
                }}
              >
                <Text style={styles.addText}>+</Text>
              </TouchableOpacity>
              <View style={{ marginTop: 50 }}></View>
            </>
          }
          keyExtractor={(item) => item.id}
        />
      </View>
      <View style={styles.footer}>
        <FooterScreen navigation={navigation} />
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    backgroundColor: "white",
  },
  innerContainer: {
    paddingHorizontal: 20,
  },
  header: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: 20,
  },
  logo: {
    width: 70,
    height: 70,
    resizeMode: "contain",
    marginLeft: 30,
  },
  helpButton: {
    width: 30,
    height: 30,
    borderRadius: 15,
    borderColor: "#BAC0CA",
    borderWidth: 1,
    justifyContent: "center",
    alignItems: "center",
  },
  helpIcon: {
    fontSize: 16,
  },
  title: {
    fontSize: 25,
    fontWeight: "bold",
    marginBottom: 5,
    marginTop: 30,
  },
  subtitle: {
    fontSize: 16,
    color: "#7B7B7B",
    marginBottom: 20,
  },
  rightImage: {
    alignSelf: "flex-end",
    width: 140,
    height: 120,
    marginBottom: 20,
  },
  imageText: { flexDirection: "row" },
  bankName: {
    marginTop: 6,
    fontSize: 18,
  },
  bankContainer: {
    // flexDirection: "row",
    // alignItems: "center",
    borderWidth: 1,
    borderColor: "#FCAF17",
    borderRadius: 20,
    padding: 15,
    marginBottom: 20,

    elevation: 5,
    backgroundColor: "white",
  },
  bankIcon: {
    width: 40,
    height: 40,
    resizeMode: "contain",
    marginRight: 10,
  },

  addButton: {
    flexDirection: "row",
    justifyContent: "center",
    alignItems: "center",
    borderWidth: 1,
    borderColor: "#FCAF17",
    borderRadius: 15,
    height: 50,
    borderStyle: "dashed", // 점선 테두리 추가
    marginVertical: 10, // 위아래로 여백 추가
  },
  addText: {
    color: "#FCAF17",
    fontSize: 24,
    fontWeight: "bold",
  },
  footer: {
    flex: 1,
    justifyContent: "flex-start",
    alignItems: "center",
    // marginTop: 220,
    marginBottom: -20,
  },
  bankTextContainer: {
    flex: 1,
    justifyContent: "space-between",
    // marginTop: 20,
    paddingHorizontal: 10,
  },

  balanceRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    marginTop: 30,
  },

  bankBalance: {
    // 기존 스타일에서 marginLeft: 'auto' 삭제
    fontSize: 16,
    fontWeight: "bold",
  },
});
