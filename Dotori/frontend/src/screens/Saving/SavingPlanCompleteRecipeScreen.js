import React, { useEffect, useState } from "react";
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  Image,
  ScrollView,
} from "react-native";

import HeaderComponent from "../Components/HeaderScreen";
import { useDispatch, useSelector } from "react-redux";
import { planSpecificationDetail } from "../../apis/planapi"

export default function SavingPlanCompleteRecipeScreen({ route, navigation }) {
  // 토큰
  const grantType =  useSelector((state)=>state.user.grantType)
  const accessToken =  useSelector((state)=>state.user.accessToken)
  const refreshToken =  useSelector((state)=>state.user.refreshToken)
  const dispatch = useDispatch()
  // 그 외
  
  // route.params에서 선택한 계좌와 명세서 번호(receipeSeq)를 가져옴
  const [accountName, setAccountName] = useState(route.params.accountName)
  const [planSeq, setPlanSeq] = useState(route.params.planSeq)
  const [accountSeq, setAccountSeq] = useState(route.params.accountSeq)

  // 가상 데이터 - 명세서 항목들
  const [receipeItems, setReceipeItems] = useState([
    { categoryTitle: "식비", expense: 50000, savings: 20000 },
    { categoryTitle: "주거", expense: 30000, savings: 10000 },
    { categoryTitle: "주거", expense: 30000, savings: 10000 },
    { categoryTitle: "주거", expense: 30000, savings: 10000 },
    { categoryTitle: "주거", expense: 30000, savings: 10000 },
    { categoryTitle: "주거", expense: 30000, savings: 10000 },
    { categoryTitle: "주거", expense: 30000, savings: 10000 },
    { categoryTitle: "주거", expense: 30000, savings: 10000 },
    { categoryTitle: "주거", expense: 30000, savings: 10000 },

    // 다른 항목들 추가
  ])

  // 추가 저축 항목
  // const additionalSavings = { categoryTitle: "추가 저축", savings: 15000 };

  // 총계 계산
  const totalExpense = receipeItems.reduce(
    (acc, item) => acc + item.expense,
    0
  );
  const totalSavings = receipeItems.reduce(
    (acc, item) => acc + item.savings,
    0
  );
  // const totalAdditionalSavings = additionalSavings.savings;


  const doPlanSpecificationDetail = async () =>{
    try{
      const response = await planSpecificationDetail(planSeq, accessToken, grantType)
      if(response.status === 200){
        setReceipeItems(response.data)
        console.log("명세서 상세 조회하기 성공", response.data)
      }else{
        console.log("명세서 상세 조회하기 실패", response.status)
      }
    }catch(error){
      console.log("오류 발생 명세서 상세 조회하기 실패: ", error)
    }
  }

  useEffect(()=>{
    // doPlanSpecificationDetail()
  },[])

  return (
    <View style={styles.container}>
      <HeaderComponent
        title="명세서 보기"
        navigation={navigation}
      ></HeaderComponent>

      {/* 선택한 명세서를 화면에 표시 */}
      <ScrollView contentContainerStyle={styles.contentContainer}>
        {/* 명세서 표 */}
        <View style={styles.receipeTable}>
          {/* 계좌 별칭 */}
          <View style={styles.topContainer}>
            <Text style={styles.label}>{accountName}</Text>
            <Text style={styles.amount}>
              {/* {(totalSavings + additionalSavings.savings).toLocaleString()}원 */}
              {(totalSavings).toLocaleString()}원
            </Text>
          </View>

          {/* 제목 행 */}
          <View style={styles.tableRow}>
            <Text style={styles.tableIndexCell}></Text>
            <Text style={styles.tableCell}>카테고리</Text>
            <Text style={styles.tableCell}>지출</Text>
            <Text style={styles.tableCell}>저축</Text>
          </View>

          {/* 구분선 */}
          <View style={styles.tableLine} />

          {/* 항목들 */}
          {receipeItems.map((item, index) => (
            <View style={styles.tableRow} key={index}>
              <Text style={styles.tableIndexCell}>{index + 1}</Text>
              <Text style={styles.tableCell}>{item.categoryTitle}</Text>
              <Text style={styles.tableCell}>
                {item.expense.toLocaleString()}
              </Text>
              <Text style={styles.tableCell}>
                {item.savings.toLocaleString()}
              </Text>
            </View>
          ))}

          {/* 구분선 */}
          <View style={styles.tableLine} />

          {/* 사용 합계 */}
          <View style={styles.tableRow}>
            <Text style={styles.tableIndexCell}></Text>
            <Text style={styles.tableCell}></Text>
            <Text style={styles.tableCell}>
              {totalExpense.toLocaleString()}
            </Text>
            <Text style={styles.tableCell}>
              {totalSavings.toLocaleString()}
            </Text>
          </View>

          {/* 추가 저축 항목 */}
          {/* <View style={styles.tableRow}>
            <Text style={styles.tableIndexCell}></Text>
            <Text style={styles.tableCell}>{additionalSavings.categoryTitle}</Text>
            <Text style={styles.tableCell}></Text>
            <Text style={styles.tableCell}>
              {additionalSavings.savings.toLocaleString()}
              {additionalSavings.savings.toLocaleString()}
            </Text>
          </View> */}

          {/* 구분선 */}
          {/* <View style={styles.tableDotLine} /> */}

          {/* 총계 행 */}
          <View style={styles.tableRow}>
            <Text style={styles.tableIndexCell}></Text>
            <Text style={styles.tableCell}>총계</Text>
            <Text style={styles.tableCell}>
              {totalExpense.toLocaleString()}
            </Text>
            <Text style={styles.tableCell}>
              {/* {(totalSavings + additionalSavings.savings).toLocaleString()} */}
              {(totalSavings).toLocaleString()}
            </Text>
          </View>
          <View style={styles.tableLine} />
        </View>
      </ScrollView>

      {/* 저축하기 버튼 */}
      <View style={{flexDirection:'row', justifyContent:"space-between"}}>
        <TouchableOpacity
          style={[styles.button, {backgroundColor:"#FF965C"}]}
          onPress={() => {
            navigation.reset({
              index: 0,
              routes: [{ name: 'MainPageScreen' }],
            });
          }}
        >
          <Text style={styles.buttonText}>종료하기</Text>
        </TouchableOpacity>
        <TouchableOpacity
          style={[styles.button, {backgroundColor:"#FF965C"}]}
          onPress={() => {
            navigation.navigate("",{})
          }}
        >
          <Text style={styles.buttonText}>저축하기</Text>
        </TouchableOpacity>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: "white",
    padding: 16,
  },
  header: {
    flexDirection: "row",
    alignItems: "center",
    marginBottom: 16,
    marginTop: 15,
  },
  arrowIcon: {
    width: 20,
    height: 20,
  },
  contentContainer: {
    flexGrow: 1, // 스크롤 가능하도록 설정
    alignItems: "center",
    paddingTop: 20,
    paddingBottom: 150, // 하단 여백
    marginTop: 40,
  },
  label: {
    fontSize: 24,
    textAlign: "left",
    // fontWeight: "bold",
    marginBottom: 8,
    marginTop: 20,
    color: "#333",
  },
  amount: {
    fontSize: 40,
    textAlign: "left",
    fontWeight: "bold",
    marginBottom: 20,
    color: "#FF965C",
  },
  receipeTable: {
    width: "90%",
    borderWidth: 1,
    borderColor: "#DDD",
    shadowColor: "rgba(0, 0, 0, 0.5)", // 그림자 색상을 어둡게 조정
    shadowOffset: { width: 0, height: 4 }, // 그림자 오프셋을 크게 조정
    shadowOpacity: 1, // 그림자 투명도를 최대로 조정
    shadowRadius: 8, // 그림자 반경을 크게 조정
    backgroundColor: "white",
    // borderTop: 20,
    paddingBottom: 40,
  },
  topContainer: {
    margin: 30,
  },
  tableHeader: {
    flexDirection: "row",
    backgroundColor: "#F5F5F5",
    borderBottomWidth: 1,
    borderColor: "#DDD",
  },
  columnHeader: {
    flex: 1,
    padding: 12,
    fontWeight: "bold",
    textAlign: "center",
  },
  tableRow: {
    flexDirection: "row",
    borderBottomWidth: 0,
    borderColor: "#DDD",
  },
  tableIndexCell: {
    flex: 0.5,
    padding: 12,
    textAlign: "center",
  },
  tableCell: {
    flex: 1,
    padding: 12,
    textAlign: "center",
  },
  tableLine: {
    height: 1,
    backgroundColor: "#000000",
    marginHorizontal: "5%",
  },
  tableDotLine: {
    height: 1,
    borderTopWidth: 1,
    borderColor: "#000000",
    borderStyle: "dashed",
    marginHorizontal: "5%",
  },
  totalCell: {
    fontWeight: "bold",
    backgroundColor: "#F5F5F5",
  },
  button: {
    height: 40,
    backgroundColor: "#FF965C",
    borderRadius: 8,
    justifyContent: "center",
    alignItems: "center",
  },
  buttonText: {
    color: "white",
    fontSize: 18,
  },
});
