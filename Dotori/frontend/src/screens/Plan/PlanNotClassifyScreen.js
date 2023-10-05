import React, { useState, useMemo, useEffect } from "react";
import {
  View,
  Text,
  StyleSheet,
  TouchableOpacity,
  ScrollView,
  FlatList,
  Modal,
} from "react-native";
import HeaderComponent from "../Components/HeaderScreen";
import { useDispatch, useSelector } from "react-redux";
import { useIsFocused } from "@react-navigation/native";
import { unclassifiedList, unClassifiedUpdate } from "../../apis/planapi";

export default function PlanNotClassifyScreen({ navigation, route }) {
  // 토큰
  const grantType = useSelector((state) => state.user.grantType);
  const accessToken = useSelector((state) => state.user.accessToken);
  const refreshToken = useSelector((state) => state.user.refreshToken);
  const dispatch = useDispatch();
  const isFocused = useIsFocused();
  const [unClassifiedList, setUnClassifiedList] = useState([]);
  const planSeq = route.params.planSeq;

  // 그 외
  const accountName = route.params.accountName;
  // TODO: 서버에서 데이터를 가져와 아래 변수들을 설정하세요
  const samepleData2 = [
    {
      planDetailSeq: 1,
      categoryName: "냥냥이 식비",
      paymentSeq: 2,
      paymentName: "냥냥밥",
      paymentPrice: 30000,
      paymentDate: "2023-08-12 16:30",
    },
    {
      planDetailSeq: 2,
      categoryName: "배달비",
      paymentSeq: 3,
      paymentName: "요기요",
      paymentPrice: 30030,
      paymentDate: "2023-08-13 15:30",
    },
    {
      planDetailSeq: 2,
      categoryName: "배달비",
      paymentSeq: 4,
      paymentName: "배달의 민족",
      paymentPrice: 32030,
      paymentDate: "2023-08-15 15:30",
    },
  ];
  const [data, setData] = useState([]); // 이 부분 추가
  const [updateData, setUpdateData] = useState([]);
  const [categoryMapping, setCategoryMapping] = useState({});

  const doUnClassifiedList = async () => {
    try {
      const response = await unclassifiedList(planSeq, accessToken, grantType);
      if (response.status === 200) {
        setUnClassifiedList(response.data);
        setData(response.data);
      } else {
        console.log("미분류 정보 조회 실패", response.status);
      }
    } catch (error) {
      console.error("오류 발생 : 미분류 정보 조회 실패:", error);
    }
  };

  const doUnClassifiedUpdate = async () => {
    try {
      console.log("updateData:", updateData);
      const response = await unClassifiedUpdate(
        updateData,
        planSeq,
        accessToken,
        grantType
      );
      if (response.status === 200) {
      } else {
        console.log("계획 정보 조회 실패", response.status);
      }
    } catch (error) {
      console.error("오류 발생 : 계획 정보 조회 실패:", error);
    }
  };

  useEffect(() => {
    // 서버로부터 데이터를 가져오는 코드가 들어갈 위치입니다.
    if (isFocused) {
      doUnClassifiedList();
      //samleData2 => unClassifiedList
      const mapping = {};
      unClassifiedList.forEach((item) => {
        if (!mapping[item.categoryName]) {
          mapping[item.categoryName] = item.planDetailSeq;
        }
        console.log("mapping", mapping);
      });
      setCategoryMapping(mapping);
      // 첫 번째 categoryGroupName을 default로 선택합니다.
      setSelectedCategoryName(unClassifiedList[0]?.categoryName || null);
    }
  }, [isFocused]); // 이 빈 배열은 이 useEffect가 컴포넌트가 마운트될 때만 실행되게 합니다.

  const onCategoryChange = (paymentSeq, newCategory, newPlanDetailSeq) => {
    // const newPlanDetailSeq = categoryMapping[newCategory];
    console.log("categoryMapping", categoryMapping);
    console.log("newCategory", newCategory);
    console.log("newPlanDetailSeq", newPlanDetailSeq);

    const newData = data.map((item) =>
      item.paymentSeq === paymentSeq
        ? {
            ...item,
            categoryName: newCategory,
            planDetailSeq: newPlanDetailSeq,
          }
        : item
    );
    setData(newData);
    console.log("newPlanDetailSeq", newPlanDetailSeq);

    console.log("newData:", newData);

    const updatedUpdateData = [
      ...updateData,
      { planDetailSeq: newPlanDetailSeq, paymentSeq: paymentSeq },
    ];
    setUpdateData(updatedUpdateData);
    console.log("updateData", updateData);
    // console.log("newData:"newData);
  };

  // uniqueCategories 정의
  const uniqueCategories = useMemo(() => {
    return [...new Set(unClassifiedList.map((item) => item.categoryName))];
  }, [unClassifiedList]);

  const formatNumber = (num) =>
    num.toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,");

  const handleNextButton = () => {
    // doPlanNewRegister();
    doUnClassifiedUpdate();
    // navigation.navigate("PlanMainScreen", {
    //   accountSeq: planInfo.accountSeq,
    // });
    navigation.goBack();
  };
  // 기존의 sampleData 대신 sampleData2 사용
  const [selectedCategoryName, setSelectedCategoryName] = useState(
    unClassifiedList[0]?.categoryName || null
  );

  // 적절한 카테고리에 해당하는 아이템들을 얻기 위한 함수 변경
  const getCategoryItems = () =>
    data.filter((item) => item.categoryName === selectedCategoryName) || [];

  return (
    <View style={styles.container}>
      <HeaderComponent
        title="미분류 항목"
        cancelNavi="PlanMainScreen"
        navigation={navigation}
      ></HeaderComponent>
      <View style={styles.innerContainer}>
        <Text style={styles.accountName}>{accountName}</Text>
        <View style={styles.scrollViewContainer}>
          <ScrollView horizontal showsHorizontalScrollIndicator={false}>
            {uniqueCategories.map((category) => (
              <TouchableOpacity
                key={category}
                onPress={() => setSelectedCategoryName(category)}
                style={
                  selectedCategoryName === category
                    ? styles.selectedGroupName
                    : styles.groupName
                }
                // ellipsizeMode="tail" // 'tail'은 문자열의 끝에서 짤림을 의미합니다.
                // numberOfLines={1} // 텍스트를 한 줄로 제한합니다.
              >
                <Text
                  style={
                    selectedCategoryName === category
                      ? styles.selectedGroupText
                      : styles.groupText
                  }
                >
                  {category}
                </Text>
              </TouchableOpacity>
            ))}
          </ScrollView>
        </View>

        <View style={styles.divider} />
        {/* // FlatList 부분 변경 */}
        <FlatList
          data={getCategoryItems()}
          renderItem={({ item }) => (
            <View style={styles.itemContainer}>
              <View style={styles.itemLeftContainer}>
                <View style={styles.transactionInfo}>
                  <Text style={styles.transactionDetail}>
                    {item.paymentName}
                  </Text>
                  <CategoryBox
                    item={item}
                    onCategoryChange={onCategoryChange}
                    uniqueCategories={uniqueCategories}
                    categoryMapping={categoryMapping} // 추가
                  />
                </View>

                <View style={styles.transactionInfo}>
                  <Text style={styles.transactionAt}>{item.paymentDate}</Text>
                  <Text style={styles.amount}>
                    {formatNumber(item.paymentPrice)}원
                  </Text>
                </View>
              </View>
            </View>
          )}
          keyExtractor={(item, index) => index.toString()}
        />
      </View>
      <TouchableOpacity style={styles.button} onPress={handleNextButton}>
        <Text style={styles.buttonText}>완료</Text>
      </TouchableOpacity>
    </View>
  );
}

function CategoryBox({
  item,
  onCategoryChange,
  uniqueCategories,
  categoryMapping,
}) {
  const [modalVisible, setModalVisible] = useState(false);

  return (
    <>
      <TouchableOpacity
        style={styles.categoryBox}
        onPress={() => setModalVisible(true)}
      >
        <Text style={styles.categoryText}>{item.categoryName}</Text>
      </TouchableOpacity>

      {modalVisible && (
        <Modal
          transparent={true}
          animationType="slide"
          visible={modalVisible}
          onRequestClose={() => setModalVisible(false)}
        >
          <View style={styles.modalOverlay}>
            <View style={styles.modalContainer}>
              <View style={styles.modalHeader}>
                <Text style={styles.modalHeaderText}>카테고리 변경</Text>
                <TouchableOpacity onPress={() => setModalVisible(false)}>
                  <Text style={styles.modalCloseText}>X</Text>
                </TouchableOpacity>
              </View>

              <ScrollView contentContainerStyle={styles.modalContent}>
                {uniqueCategories.map((category) => (
                  <TouchableOpacity
                    key={category}
                    style={styles.modalItem}
                    onPress={() => {
                      const newPlanDetailSeq = categoryMapping[category];
                      setModalVisible(false);
                      onCategoryChange(
                        item.paymentSeq,
                        category,
                        newPlanDetailSeq
                      );
                    }}
                  >
                    <Text style={styles.modalItemText}>{category}</Text>
                  </TouchableOpacity>
                ))}
              </ScrollView>
            </View>
          </View>
        </Modal>
      )}
    </>
  );
}

const styles = StyleSheet.create({
  modalOverlay: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "rgba(0,0,0,0.5)",
  },
  modalContainer: {
    width: "80%",
    maxHeight: "50%", // 화면 중간까지 올라오게 설정
    backgroundColor: "white",
    borderRadius: 10,
    padding: 20,
    overflow: "hidden",
  },
  modalHeader: {
    flexDirection: "row",
    justifyContent: "space-between",
    alignItems: "center",
    marginBottom: 10,
  },
  modalHeaderText: {
    fontSize: 16,
    fontWeight: "bold",
  },
  modalCloseText: {
    fontSize: 16,
    fontWeight: "bold",
  },
  modalContent: {
    alignItems: "center",
  },
  modalItem: {
    width: "100%",
    backgroundColor: "#727070",
    borderRadius: 10,
    marginBottom: 10,
    padding: 10,
  },
  modalItemText: {
    color: "white",
    fontSize: 14,
  },
  container: {
    flex: 1,
    // alignItems: "center",
    backgroundColor: "white",
    padding: 16,
  },
  innerContainer: {
    // alignItems: "center",
    flex: 1,
    // marginBottom: -100,
  },
  accountName: {
    fontWeight: "bold",
    alignSelf: "flex-start",
    fontSize: 24,
    marginLeft: 16,
    marginTop: 20,
    marginBottom: 20,
  },
  groupName: {
    padding: 10,
    paddingBottom: 5,
    // height: 100,
  },

  selectedGroupName: {
    borderBottomWidth: 5,
    borderBottomColor: "#FF965C",
    padding: 10,
    paddingBottom: 5,
    // height: 100,
    // marginHorizontal: 10,
    // marginVertical: 10,
  },
  groupText: {
    color: "#727070",
    marginBottom: 10,
  },
  selectedGroupText: {
    color: "#FF965C",
    // paddingBottom: 10,
    marginBottom: 10,
  },
  divider: {
    height: 1,
    backgroundColor: "#F5F5F5",
    width: "100%",
    marginVertical: 10,
    marginBottom: 30,
  },
  itemContainer: {
    flexDirection: "row",
    // justifyContent: "space-between",
    alignItems: "center",
    padding: 10,
    borderColor: "#E3E3E3",
    borderWidth: 1,
    borderRadius: 15,
    marginVertical: 5,
    marginLeft: 20,

    width: "90%",

    elevation: 5,
    backgroundColor: "white",
  },
  itemLeftContainer: {
    flex: 1,
    justifyContent: "space-between",
    marginLeft: 10,
    marginRight: 10,
  },
  transactionInfo: {
    flexDirection: "row",
    justifyContent: "space-between",
    marginTop: 5,
  },
  transactionDetail: {
    flex: 1,
    fontWeight: "bold",
    marginBottom: 10,
  },
  categoryBox: {
    backgroundColor: "#F2F5F8",
    borderRadius: 10,
    padding: 5,
    // marginTop: -20,
    paddingTop: 6,
    marginBottom: 3,
  },
  transactionAt: {
    color: "#7D7D7D",
    fontSize: 12,
  },
  amount: {
    textAlign: "right",
    fontWeight: "bold",
  },
  categoryText: {
    fontSize: 10,
  },
  button: {
    height: 50,
    backgroundColor: "#FF965C",
    borderRadius: 8,
    justifyContent: "center",
    alignItems: "center",
    marginTop: 10,
  },
  buttonText: {
    color: "white",
    fontSize: 18,
  },
});
