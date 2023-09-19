import React, { useEffect, useState } from "react";
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  FlatList,
  Modal,
  ScrollView,
} from "react-native";
import { Ionicons, EvilIcons, Octicons } from "@expo/vector-icons";
import HeaderScreen from "../Header/HeaderScreen";
import { accountTransactionDetail, accountTransactionHistory } from "../../apis/accountapi"
import { useSelector } from "react-redux";

export default function AccountDetail({ navigation, route }) {
  const [account, setAccount] = useState(route.params.account)
  const accessToken = useSelector((state) => state.user.accessToken)
  const [historySearchMonth, setHistorySearchMonth] = useState(3)// 1,2,3,4,5,6 등 개월 수로 현재는 6개월 까지만
  const [historyTransactionType, setHistoryTransactionType] = useState("ALL")// "DEPOSIT", "WITHDRAWAL", "ALL"
  const [historySortType, setHistorySortType] = useState(0)// 0DES// page는 0부터 시작C최신순 1ASC과거순
  const [historyPage, setHistoryPage] = useState(0)
  const [nextPage, setNextPage] = useState(1)
  const [typeChange, setTypeChange] = useState(false)
  // const [sendHistoryUnits, setSendHistoryUnits] = useState({
  //   searchMonth : historySearchMonth,
  //   transactionType : historyTransactionType,
  //   sortType : historySortType,
  //   page : historyPage,
  // })
  const [accountInfo, setAccountInfo] = useState(null)
  const [transactionHistory, setTransactionHistory] =  useState([])

  // 현재 날짜를 가져오기
  const currentDate = new Date();
  // n개월 전 날짜를 계산
  let selectMonthsAgo = new Date(currentDate);
  selectMonthsAgo.setMonth(selectMonthsAgo.getMonth() - historySearchMonth);

  // 날짜를 "YYYY.MM.DD" 형식으로 변환하는 함수
  const formatDate = (date) => {
    return `${date.getFullYear()}.${date.getMonth() + 1}.${date.getDate()}`;
  };

  const [modalVisible, setModalVisible] = useState(false); // 모달 창 표시 상태 관리
  const [period, setPeriod] = useState("3개월"); // 기간 선택 상태
  const [range, setRange] = useState("전체"); // 범위 선택 상태
  const [order, setOrder] = useState("최신순"); // 정렬 순서 상태

  const formDateTransaction = (dateString) =>{
    const date = new Date(dateString);
    const year = date.getFullYear();
    const month = (date.getMonth() + 1).toString().padStart(2, "0");
    const day = date.getDate().toString().padStart(2, "0");
    const hours = date.getHours().toString().padStart(2, "0");
    const minutes = date.getMinutes().toString().padStart(2, "0");
    const seconds = date.getSeconds().toString().padStart(2, "0");
    return `${year}.${month}.${day} ${hours}:${minutes}:${seconds}`;
  }

  // 계좌 상세 조회
  const getAccountTransactionDetail = async ()=>{
    const response = await accountTransactionDetail(account.accountNumber, accessToken)
    if(response.status===200){
      console.log('계좌 상세조회에 성공했습니다.')
      setAccountInfo(response.data)
    }else if(response.status===400){
      console.log('bad400 계좌 상세조회 실패로 계좌 목록을 받아올 수 없습니다.')
    }else if(response.status===401){
      console.log('bad401 권한 없음으로 계좌 상세조회 할 수 없습니다.')
    }else if(response.status===404){
      console.log('bad404 계좌가 존재하지 않아 계좌 상세조회 할 수 없습니다.')
    }else{
      console.log('오류발생: 계좌 상세내역을 받아올 수 없습니다.')
    }
  }


  // type 변경후 거래 내역 조회 
  const handlegetAccountTransactionHistory = async (searchMonth, transactionType, sortType)=>{
    // const sendHistoryUnits = {
    const sendHistoryUnitss = {
      searchMonth : searchMonth,
      transactionType : transactionType,
      sortType : sortType,
      page : 0,
    }
    console.log('###########')
    console.log('샌드히스토리',typeChange,sendHistoryUnitss)
    console.log('###########')
    // const response = await accountTransactionHistory(sendHistoryUnits, account.accountNumber, accessToken)
    const response = await accountTransactionHistory(sendHistoryUnitss, account.accountNumber, accessToken)
    if(response.status===200){
      console.log('계좌 거래내역 조회에 성공했습니다.')
      const newData = response.data
      console.log("리스폰스데이터",response.data)
      if ((newData.length > 0)) {
        setTransactionHistory([])
        setTransactionHistory(newData)
        setHistoryPage(1)
      }else{
        setTransactionHistory([])
        setHistoryPage(0)
      }
    }else if(response.status===400){
      console.log('bad400 거래내역 조회에 실패했습니다.')
    }else if(response.status===401){
      console.log('bad401 권한 없음으로 계좌 거래내역을 조회 할 수 없습니다.')
    }else if(response.status===404){
      console.log('bad404 계좌가 존재하지 않아 계좌 거래내역 조회 할 수 없습니다.')
    }else{
      console.log('오류발생: 계좌 거래내역을 받아올 수 없습니다.')
    }
  }
  // 거래 내역 조회
  const getAccountTransactionHistory = async ()=>{
    // const sendHistoryUnits = {
    const sendHistoryUnitss = {
      searchMonth : historySearchMonth,
      transactionType : historyTransactionType,
      sortType : historySortType,
      page : historyPage,
    }
    console.log('###########')
    console.log('샌드히스토리',typeChange,sendHistoryUnitss)
    console.log('###########')
    // const response = await accountTransactionHistory(sendHistoryUnits, account.accountNumber, accessToken)
    const response = await accountTransactionHistory(sendHistoryUnitss, account.accountNumber, accessToken)
    if(response.status===200){
      console.log('계좌 거래내역 조회에 성공했습니다.')
      const newData = response.data
      console.log("리스폰스데이터",response.data)
      if ((newData.length > 0)) {
        // 기존 transactionHistory 배열에 새 데이터를 덧붙입니다.
        setTransactionHistory((prevData) => [...prevData, ...newData])
        setHistoryPage(historyPage+1)
      }
    }else if(response.status===400){
      console.log('bad400 거래내역 조회에 실패했습니다.')
    }else if(response.status===401){
      console.log('bad401 권한 없음으로 계좌 거래내역을 조회 할 수 없습니다.')
    }else if(response.status===404){
      console.log('bad404 계좌가 존재하지 않아 계좌 거래내역 조회 할 수 없습니다.')
    }else{
      console.log('오류발생: 계좌 거래내역을 받아올 수 없습니다.')
    }
  }

  const settingAccountNumber = (accountNumber) =>{
    return `${accountNumber.slice(0,4)}-${accountNumber.slice(4,6)}-${accountNumber.slice(6)}`
  }

  useEffect(()=>{
    getAccountTransactionDetail()
    getAccountTransactionHistory()
  },[])

  return (
    <View style={styles.container}>
      <HeaderScreen navigation={navigation} title="거래 내역 조회" />
      <View style={styles.nicknameContainer}>
        <View style={styles.row}>
          {/* 계좌 nickname 차후 바꿀 수 있게 할 것. */}
          <Text style={styles.nickname}>나의 첫번째 계좌</Text>
          <TouchableOpacity
            onPress={()=>{}}
          >
            <EvilIcons
              name="pencil"
              size={20}
              color="black"
              style={{ marginTop: 20 }}
            />
          </TouchableOpacity>
        </View>

        <TouchableOpacity
          style={styles.setting}
          onPress={() => {
            navigation.navigate("AccountManagementScreen", {account:account});
          }}
        >
          {/* <EvilIcons name="gear" size={24} color="black" /> */}
          <Octicons name="gear" size={16} color="black" />
        </TouchableOpacity>
      </View>
      
      {accountInfo
        // ?(<><Text style={styles.accountNumber}>{accountInfo.accountNumber.slice(0,4)}-{accountInfo.accountNumber.slice(4,6)}-{accountInfo.accountNumber.slice(6)}</Text>
        ?(<><Text style={styles.accountNumber}>{settingAccountNumber(accountInfo.accountNumber)}</Text>
        <Text style={styles.balance}>
          {(Math.floor(accountInfo.balance)).toLocaleString()}원
        </Text></>)
        :(<><Text style={styles.accountNumber}>1234-12-1234567</Text>
        <Text style={styles.balance}>
          2,500,000원
        </Text></>)
      }
      
      <View style={styles.horizontalSeparator} />
      {/* 계좌 내역 조회 방식 선택 */}
      <View style={styles.queryModeContainer}>
        <TouchableOpacity
          onPress={() => setModalVisible(true)}
          style={{ flexDirection: "row", alignItems: "center", width: "100%" }}
        >
          <Text style={styles.queryText}>{period} · </Text>
          <Text style={styles.queryText}>{range} · </Text>
          <Text style={styles.queryText}>{order}</Text>
          <Ionicons name="chevron-forward" size={24} color="black" />
        </TouchableOpacity>
      </View>
      <Modal 
        animationType="slide" 
        transparent={true} 
        visible={modalVisible} 
        onRequestClose={() => {
          // Alert.alert('Modal has been closed.');
          // setUserWithdrawalModalVisible(!userWithdrawalModalVisible);
          setModalVisible(false)
        }}>
        <View
          style={{ flex: 1, justifyContent: "flex-end", alignItems: "center" }}
        >
          <View
            style={{
              width: "100%",
              backgroundColor: "white",
              borderTopLeftRadius: 20,
              borderTopRightRadius: 20,
              padding: 20,
              borderStyle:"solid",
              borderWidth: 2,
            }}
          >
            <View
              style={{
                flexDirection: "row",
                justifyContent: "space-between",
                marginBottom: 20,
              }}
            >
              <Text style={styles.modalSelectText}>조회기간 설정</Text>
              <TouchableOpacity onPress={() => setModalVisible(false)}>
                <Text style={{ fontSize: 20 }}>X</Text>
              </TouchableOpacity>
            </View>
            <Text>기간 선택</Text>
            <View
              style={{
                flexDirection: "row",
                justifyContent: "space-around",
                marginVertical: 10,
                borderWidth: 1,
                borderColor: "#757575",
              }}
            >
              <Text style={{color: historySearchMonth === 1 ? "red" :"black"}} 
                onPress={() => {
                  setPeriod("1개월") 
                  setHistorySearchMonth(1)
                  // setSendHistoryUnits((prev) => ({ ...prev, searchMonth: 1 }));
                }
              }>1개월</Text>
              <Text style={{color: historySearchMonth === 3 ? "red" :"black"}} 
                onPress={() => {
                  setPeriod("3개월") 
                  setHistorySearchMonth(3)
                  // setSendHistoryUnits((prev) => ({ ...prev, searchMonth: 3 }));
                }}
              >3개월</Text>
              <Text style={{color: historySearchMonth === 6 ? "red" :"black"}} 
                onPress={() => {
                  setPeriod("6개월") 
                  setHistorySearchMonth(6)
                  // setSendHistoryUnits((prev) => ({ ...prev, searchMonth: 6 }));
                }}
              >6개월</Text>
              <Text style={{color: historySearchMonth === 12 ? "red" :"black"}} 
                onPress={() => {
                  setPeriod("1년") 
                  setHistorySearchMonth(12)
                  // setSendHistoryUnits((prev) => ({ ...prev, searchMonth: 12 }));
                }}
              >1년</Text>
              <Text style={{color: historySearchMonth === 24 ? "red" :"black"}} 
                onPress={() => {
                  setPeriod("2년") 
                  setHistorySearchMonth(24)
                  // setSendHistoryUnits((prev) => ({ ...prev, searchMonth: 24 }));
                }}
              >2년</Text>
              {/* 한동안 직접입력은 없는것으로..... */}
              {/* <Text onPress={() => setPeriod("직접입력")}>직접입력</Text> */}
            </View>
            <Text>거래 유형</Text>
            <View
              style={{
                flexDirection: "row",
                justifyContent: "space-around",
                marginVertical: 10,
                borderWidth: 1,
                borderColor: "#757575",
              }}
            >
              <Text style={{color: historyTransactionType === "ALL" ? "red":"black"}} 
                onPress={() => {
                  setRange("전체"), setHistoryTransactionType('ALL')
                  // setSendHistoryUnits((prev) => ({ ...prev, transactionType: "ALL" }));
                }}
              >전체</Text>
              <Text style={{color: historyTransactionType === "DEPOSIT" ? "red":"black"}} 
                onPress={() => {
                  setRange("입금"), setHistoryTransactionType('DEPOSIT')
                  // setSendHistoryUnits((prev) => ({ ...prev, transactionType: "DEPOSIT" }));
                }}
              >입금</Text>
              <Text style={{color: historyTransactionType === "WITHDRAWAL" ? "red":"black"}} 
                onPress={() => {
                  setRange("출금"), setHistoryTransactionType('WITHDRAWAL')
                  // setSendHistoryUnits((prev) => ({ ...prev, transactionType: "WITHDRAWAL" }));
                }}
              >출금</Text>
            </View>
            <Text>정렬 순</Text>
            <View
              style={{
                flexDirection: "row",
                justifyContent: "space-around",
                marginVertical: 10,
                borderWidth: 1,
                borderColor: "#757575",
              }}
            >
              <Text style={{color: historySortType===0?"red":"black"}} 
                onPress={() => {
                  setOrder("최신순"), setHistorySortType(0)
                  // setSendHistoryUnits((prev) => ({ ...prev, sortType: 0 }));
                }}
              >최신순</Text>
              <Text style={{color: historySortType===1?"red":"black"}} 
                onPress={() => {
                  setOrder("과거순"), setHistorySortType(1)
                  // setSendHistoryUnits((prev) => ({ ...prev, sortType: 1 }));
                }}
              >과거순</Text>
            </View>
            <TouchableOpacity
              style={{
                backgroundColor: "#9D57D0",
                padding: 10,
                borderRadius: 5,
                alignItems: "center",
              }}
              onPress={()=>{
                // setTypeChange(true)
                // setHistoryPage(0),
                setModalVisible(false)
                // getAccountTransactionHistory(0)
                handlegetAccountTransactionHistory(historySearchMonth, historyTransactionType, historySortType)
              }}
            >
              <Text style={{ color: "white" }}>조회</Text>
            </TouchableOpacity>
          </View>
        </View>
      </Modal>
      <View style={styles.horizontalSeparator} />

      {/* 여기부터는 계좌 내역 관련 UI */}
      <View style={styles.dateContainer}>
        <Text style={styles.dateText}>
          {formatDate(selectMonthsAgo)} ~ {formatDate(currentDate)}
        </Text>
      </View>
      <View style={styles.boldSeparator} />
      
      
      <FlatList
        data={transactionHistory}
        renderItem={({ item }) => (
          <TouchableOpacity
            style={styles.cardContainer}
            onPress={() => {
              navigation.navigate("TransactionDetailScreen", {transactionInfo:item});
            }}
          >
            <Text style={styles.transactionDate}>{formDateTransaction(item.transactionAt)}</Text>
            <Text style={styles.place}>{item.content}</Text>
            
            <Text style={styles.transactionInfo}>
              {item.transactionType === "WITHDRAWAL" ? "출금" : "입금"}{" "}
              <Text
                style={
                  item.transactionType === "WITHDRAWAL"
                    ? styles.withdrawalAmount
                    : styles.depositAmount
                }
              >
                {item.amount.toLocaleString()}원
              </Text>
            </Text>
              
            <Text style={styles.afterBalance}>
              잔액: {item.balanceAfter.toLocaleString()}원
            </Text>
            <View style={styles.horizontalSeparator} />
            {/* 각 아이템 밑에 수평 구분선 추가 */}
          </TouchableOpacity>
        )}
        keyExtractor={(item) => `${item.transactionAt}-${item.content}-${item.amount}`}
        onEndReached={getAccountTransactionHistory}  // 스크롤의 끝에 도달했을 때 작동
        onEndReachedThreshold={0.1} // 스크롤을 얼마나 내려야만 onEndReached가 작동할지를 정함
      />

      
    </View>
  );
}

// 스타일 정의
const styles = StyleSheet.create({
  container: {
    flex: 1,
    padding: 20,
    backgroundColor: "white",
  },
  headerContainer: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "flex-start",
    paddingTop: 30,
  },
  row: {
    flexDirection: "row",
  },
  modalSelectText: {
    fontSize: 18,
  },
  modalOption: {
    marginVertical: 8,
    paddingVertical: 5,
    paddingHorizontal: 10,
    borderWidth: 1,
    borderColor: "grey",
    borderRadius: 5,
    textAlign: "center",
  },
  podoImage: {
    width: 25,
    height: 25,
    resizeMode: "contain",
    marginLeft: 20,
    marginRight: 15,
  },
  bankText: {
    fontSize: 15,
    marginRight: 15,
  },
  setting: {
    alignItems: "flex-end",
  },

  nicknameContainer: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between", // 여기를 수정했습니다.
    marginTop: 20,
    marginLeft: 20,
    marginBottom: 5,
  },

  nickname: {
    fontSize: 16,
    // fontWeight: "bold",
    textAlign: "left",
    marginTop: 20,
  },
  accountNumber: {
    fontSize: 20,
    textAlign: "left",
    fontWeight: "bold",
    marginBottom: 40,
    marginLeft: 20,
  },
  balance: {
    fontSize: 24,
    fontWeight: "bold",
    textAlign: "right",
    marginRight: 20,
    marginBottom: 20,
  },
  transferButtonContainer: {
    justifyContent: "center",
    alignItems: "center",
    marginVertical: 20,
  },
  transactionDate: {
    fontSize: 16,
    marginBottom: 5,
  },
  place: {
    fontSize: 16,
    marginBottom: 5,
  },
  afterBalance: {
    textAlign: "right",
    fontSize: 16,
    marginBottom: 20,
    color: "gray",
  },
  separator: {
    height: 1,
    backgroundColor: "gray",
    marginVertical: 10,
  },
  transactionContainer: {
    flexDirection: "column",
  },
  transactionType: {
    fontSize: 16,
    textAlign: "right",
    flex: 1,
  },
  transactionAmount: {
    color: "red",
  },
  transactionAmountContainer: {
    flexDirection: "row",
    textAlign: "right",
    justifyContent: "flex-end",
    alignItems: "center",
  },
  dateContainer: {
    marginTop: 5,
    // marginBottom: 5,
    alignItems: "flex-start",
  },
  dateText: {
    fontSize: 16,
    marginBottom: 10,
    // justifyContent: "flex-start",
  },
  horizontalSeparator: {
    height: 1,
    backgroundColor: "grey",
    marginVertical: 10,
  },
  boldSeparator: {
    height: 2,
    backgroundColor: "black",
    marginBottom: 20,
  },
  queryModeContainer: {
    flexDirection: "row",
    justifyContent: "flex-start",
    alignItems: "center",
  },
  queryText: {
    fontWeight: "bold",
  },
  cardContainer: {
    width: '100%',
    height: 100,
    borderRadius: 23,
    marginBottom: 15, // 카드 간격
    // padding: 10, // 카드 내부 패딩
    alignSelf: "center", // 가운데 정렬
    // marginLeft: -5,
    // marginRight: -5,
  },
  withdrawalAmount: {
    color: "red",
  },
  depositAmount: {
    color: "blue",
  },
  place: {
    fontWeight: "600",
    fontSize: 14,
    lineHeight: 17,
    color: "#000000",
    marginBottom: 5,
  },
  transactionDate: {
    fontWeight: "600",
    fontSize: 12,
    lineHeight: 15,
    textAlign: "right",
    color: "#000000",
    marginBottom: 5,
    textAlign: "left", // 왼쪽 정렬
  },
  transactionInfo: {
    fontWeight: "600",
    fontSize: 14,
    lineHeight: 17,
    textAlign: "right",
    color: "#000000",
    marginBottom: 5,
  },
  afterBalance: {
    fontWeight: "600",
    fontSize: 12,
    lineHeight: 15,
    textAlign: "right",
    color: "#858585",
  },
});
