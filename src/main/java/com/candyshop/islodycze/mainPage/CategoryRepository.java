package com.candyshop.islodycze.mainPage;

import com.candyshop.islodycze.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface CategoryRepository extends JpaRepository<Category, Long>{ }
